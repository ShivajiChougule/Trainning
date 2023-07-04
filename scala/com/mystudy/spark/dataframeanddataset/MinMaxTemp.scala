package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructType}
import org.apache.spark.sql.functions._
object MinMaxTemp {

  case class temp(stationId:String,date:Int,measure_type:String,temperature:Float)

  def main(args:Array[String]):Unit={

    val spark = SparkSession.builder().master("local").appName("temp").getOrCreate()

    val tempSchema = new StructType()
      .add("stationID", StringType, nullable = true)
      .add("date", IntegerType, nullable = true)
      .add("measure_type", StringType, nullable = true)
      .add("temperature", FloatType, nullable = true)

    import spark.implicits._
    val ds = spark.read
      .schema(tempSchema)
      .csv("data/1800.csv")
      .as[temp]

    val mintemp = ds.filter($"measure_type" ==="TMIN")

    val maxtemp = ds.filter($"measure_type" ==="TMAX")

    val minStaions = mintemp.select("stationID","temperature")

    val maxStaions = maxtemp.select("stationID","temperature")

    val minTempByStations = mintemp.groupBy("stationID").min("temperature")

    val maxTempByStations = maxtemp.groupBy("stationID").max("temperature")

    val mintempF = minTempByStations.withColumn("temperature",round($"min(temperature)"*0.1f*(9.0f/5.0f)+32.0f,2))
      .select("stationID","temperature").sort("temperature")

    val maxtempF = maxTempByStations.withColumn("temperature", round($"max(temperature)" * 0.1f * (9.0f / 5.0f) + 32.0f, 2))
      .select("stationID", "temperature").sort("temperature")

    val minresult = mintempF.collect()

    val maxresult = maxtempF.collect()

    minresult.foreach(println)

    maxresult.foreach(println)




  }

}
