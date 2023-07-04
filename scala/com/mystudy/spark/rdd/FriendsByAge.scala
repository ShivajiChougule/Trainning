package com.mystudy.spark.rdd

import org.apache.spark.SparkContext

object FriendsByAge {

  private def parsLine(line : String) : (Int, Int) = {
    val  fields = line.split(",")
    val age = fields(2).toInt
    val numFrd = fields(3).toInt
    (age, numFrd)
  }

  def main(args:Array[String]): Unit = {
    val sc= new SparkContext(master = "local[*]", appName = "FrdsByAge")

    sc.setLogLevel("ERROR")

    val lines = sc.textFile("data/fakefriends-noheader.csv")

    val rdd = lines.map(parsLine)

    val totalByAge = rdd.mapValues(x=>(x,1)).reduceByKey((x,y)=>(x._1 + y._1, x._2 + y._2))

    val averageByAge = totalByAge.mapValues(x=>x._1/x._2).sortBy(_._2,ascending = false)

    val results = averageByAge.collect()

    results.foreach(println)
  }

}
