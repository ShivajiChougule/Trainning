package com.mystudy.spark.rdd

import org.apache.spark.SparkContext

object WordCountRegex {

  def main(args:Array[String]): Unit = {
    val sc = new SparkContext(master = "local", appName ="WordCountRegex")

    val lines = sc.textFile("data/book.txt")

    val words = lines.flatMap(x=>x.split("\\W+"))

    val lower = words.map(x=>x.toLowerCase())

    val wordCount = lower.countByValue().toSeq.sortBy(x=>x._2).reverse

    wordCount.foreach(x=>println(f"${x._1} is ${x._2} times"))

    val totalWords = words.count()

    println(f"totalWords in book are $totalWords")


  }

}
