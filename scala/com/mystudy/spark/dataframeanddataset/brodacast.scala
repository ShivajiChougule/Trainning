package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, LongType, StructType}
import org.apache.spark.sql.functions._

import scala.io.{Codec, Source}

object brodacast {

  case class movies(userID:Int,movieID:Int,rating:Int,timestamp:Long)

  def loadMovies():Map[Int,String] ={

    implicit val codec:Codec=Codec("ISO-8859-1")

    var movieNames:Map[Int,String]=Map()

    val lines = Source.fromFile("data/ml-100k/u.item")

    for (line<-lines.getLines()){
      val fields = line.split('|')
      if (fields.length>1){
        movieNames+=(fields(0).toInt->fields(1))
      }
    }
    lines.close()

    movieNames
  }

  def main(args:Array[String]):Unit={

    val spark =SparkSession.builder().master("local").appName("broadcast").getOrCreate()

    val nameDict = spark.sparkContext.broadcast(loadMovies())

    val movieSchema = new StructType()
      .add("userID",IntegerType,nullable = true)
      .add("movieID",IntegerType,nullable = true)
      .add("rating",IntegerType,nullable = true)
      .add("timestamp",LongType,nullable = true)

    import spark.implicits._

    val fields = spark.read
      .option("sep","\t")
      .schema(movieSchema)
      .csv("data/ml-100k/u.data")
      .as[movies]

    val movieCount = fields.groupBy("movieID").count()

    println("Starting NameDict")
    nameDict.value.foreach{
      case (key, value) => println(key + " = " + value)
    }
    println("Ending NameDict")

    val lookupName:Int=>String=(movieID:Int)=>
      nameDict.value(movieID)
    val lookupNameUDF = udf(lookupName)

    val moviesWithNames = movieCount.withColumn("movieTitle",lookupNameUDF(col("movieID")))

    val sortedMoviesWithNames = moviesWithNames.sort("count")

    sortedMoviesWithNames.show(sortedMoviesWithNames.count.toInt,truncate = false)

  }

}
