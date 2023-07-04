package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession

object friendsByAgeSQL{

  private case class FriendsSchema(id:Int, name:String, age:Int, friends:Int)

  def main(args:Array[String]):Unit={

    val spark = SparkSession.builder().master("local").appName("FriendsByAge").getOrCreate()

    import spark.implicits._

    val friends = spark.read.option("header","true").option("inferSchema","true").csv("data/fakefriends.csv").as[FriendsSchema]

    friends.printSchema()

    friends.groupBy("age").avg("friends").sort("age").show()

  }

}
