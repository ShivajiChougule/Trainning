package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession

object DataFrameDataset {

  case class Person(id:Int,name:String,age:Int,friends:Int)

  def main(args:Array[String]):Unit={

    val spark = SparkSession.builder().master("local").appName("SparkSql").getOrCreate()

    import spark.implicits._

    val people = spark.read.option("header","true").option("inferSchema","true").csv("data/fakefriends.csv").as[Person]

    people.printSchema()

    println("column name")
    people.select("name").show()

    println("filtering over 21")
    people.filter(people("age")<21).show()

    println("GroupByAge")
    people.groupBy("name").count().show()

    println("make everyone 10 yr older")
    people.select(people("name"),people("age")+10 as "PeopleTenPlus").show()

    spark.stop()
  }

}
