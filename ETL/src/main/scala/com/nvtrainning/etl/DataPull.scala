package com.nvtrainning.etl

import org.apache.spark.sql.{SparkSession, Dataset}

object DataPull {
  case class WeatherData(lat: Double,
                         lon: Double,
                         city: String,
                         datetime: Long,
                         cloud: String,
                         cloud_description: String,
                         temperature: Double,
                         pressure: Int,
                         humidity: Int,
                         visibility: Int,
                         wind_speed: Double,
                         wind_direction_deg: Int,
                         cloudiness_percent: Double,
                         sunrise: Long,
                         sunset: Long)

  val token = "ddd2233366783b2c533e6082d74b2e2443da1ddc"
  val boundurl = "https://api.waqi.info/map/bounds/?latlng=7.36247,67.67578,35.88905,97.38281&token=" + token
  val response = requests.get(boundurl)
  val responseData = ujson.read(response.text)
  val data = responseData("data").arr
  val filteredData = data.filter(_.obj("station").obj("name").str.toLowerCase.contains("india"))
  val latLonList = filteredData.map { item =>
    val lat: Double = item("lat").num
    val lon: Double = item("lon").num
    (lat, lon)
  }.toList
  val apiKey = "4a7d2f914303f1681cbac2d3d9f27ce1"

  def main(args: Array[String]) = {
    val spark = SparkSession.builder().appName("WeatherData").master("local[*]").getOrCreate()
    import spark.implicits._
    val latLonDF = latLonList.toDF("lat", "lon")
    val weatherDataset: Dataset[WeatherData] = latLonDF.flatMap { row =>
      val lat = row.getAs[Double]("lat")
      val lon = row.getAs[Double]("lon")
      val apiUrl: String = s"https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$apiKey"
      val apiResponse = requests.get(apiUrl)
      val apiResponseData = ujson.read(apiResponse.text)
      val city = apiResponseData("name").str
      val datetime = apiResponseData("dt").num.toLong
      val cloud = apiResponseData("weather")(0)("main").str
      val cloud_description = apiResponseData("weather")(0)("description").str
      val temperature = apiResponseData("main")("temp").num
      val pressure = apiResponseData("main")("pressure").num.toInt
      val humidity = apiResponseData("main")("humidity").num.toInt
      val visibility = apiResponseData("visibility").num.toInt
      val wind_speed = apiResponseData("wind")("speed").num
      val wind_direction_deg = apiResponseData("wind")("deg").num.toInt
      val cloudiness_percent = apiResponseData("clouds")("all").num
      val sunrise = apiResponseData("sys")("sunrise").num.toLong
      val sunset = apiResponseData("sys")("sunset").num.toLong
      Some(WeatherData(lat,
        lon,
        city,
        datetime,
        cloud,
        cloud_description,
        temperature,
        pressure,
        humidity,
        visibility,
        wind_speed,
        wind_direction_deg,
        cloudiness_percent,
        sunrise,
        sunset))
    }.as[WeatherData]
    weatherDataset.show(weatherDataset.count.toInt)

    val jdbcUrl = "jdbc:postgresql://localhost:5432/public"
    val username = "postgres"
    val password = "pass"
    PostgresStorage.storeWeatherData(weatherDataset, "public", "weatherdata", jdbcUrl, username, password)
    spark.stop()
  }

}
