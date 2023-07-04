package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object wordCountSQL {

  case class Book(value:String)

  def main(args:Array[String]):Unit={

    val spark = SparkSession.builder().master("local").appName("WordCount").getOrCreate()

    import spark.implicits._

    val lines = spark.read.text("data/book.txt").as[Book]

    val words = lines.select(explode(split($"value","\\W+")).alias("word")).filter($"word" =!= "")

    val lowerCaseWord = words.select(lower($"word").as("word"))

    val wordcount = lowerCaseWord.groupBy("word").count()

    val wordcountSorted = wordcount.sort("count")

    wordcountSorted.show(wordcountSorted.count.toInt)

  }

}
