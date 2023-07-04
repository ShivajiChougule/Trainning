package com.mystudy.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}



object RatingCount {

  def main(args:Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("RatingCount")

    val sc = new SparkContext(conf)

    sc.setLogLevel("ERROR")

    val lines = sc.textFile(path = "data/ml-100k/u.data")

    val ratings = lines.map(x => x.split("\t")(2))

    val results = ratings.countByValue()

    val sortedResults = results.toSeq.sortBy(_._1)

    sortedResults.foreach(println)
    


  }
}
