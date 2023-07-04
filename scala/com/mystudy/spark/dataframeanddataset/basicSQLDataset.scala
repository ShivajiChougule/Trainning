package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession

object basicSQLDataset {

  case class Person(id:Int,name:String,age:Int,friends:Int)

  def main(args:Array[String]): Unit ={

    val spark = SparkSession.builder.appName("SparkSQL").master("local").getOrCreate()

    import spark.implicits._
    val schemaPepole = spark.read.option("header","true").option("inferSchema","true").csv("data/fakefriends.csv").as[Person]

    schemaPepole.createOrReplaceTempView("people")

    val teenagers = spark.sql("select * from people where age >=13 and age <=19")

    teenagers.foreach(println)

   // val results = teenagers.collect()

  //  results.foreach(println)

    spark.stop()
  }

}
