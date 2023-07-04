package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, LongType, StructType}
import org.apache.spark.sql.functions._
object MostPopularMovie {

  private case class movielens(movieID:Int)
  def main(args:Array[String]):Unit={

    val spark = SparkSession.builder().master("local").appName("movies").getOrCreate()

    import spark.implicits._
    val movieSchema = new StructType()
      .add("userId",IntegerType,nullable = true)
      .add("movieID",IntegerType,nullable = true)
      .add("ratings",IntegerType,nullable = true)
      .add("timestamp",LongType,nullable = true)

    val fields = spark.read
      .option("sep","\t")
      .schema(movieSchema)
      .csv("data/ml-100k/u.data")
      .as[movielens]

    fields.show(10)

    val popular = fields.groupBy("movieId").count().orderBy(desc("count"))

    popular.show(10)

    spark.stop()


  }


}
