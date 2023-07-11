package com.nvtrainning.etl

import java.util.Properties
import com.nvtrainning.etl.DataPull.WeatherData
import org.apache.spark.sql.Dataset

object PostgresStorage {
  def storeWeatherData(weatherDataset: Dataset[WeatherData], database: String, table: String,
                       jdbcUrl: String, username: String, password: String): Unit = {

    val connectionProperties = new Properties()
    connectionProperties.put("user", username)
    connectionProperties.put("password", password)

    weatherDataset.write.mode("append")
      .jdbc(jdbcUrl, s"$database.$table", connectionProperties)
  }
}

