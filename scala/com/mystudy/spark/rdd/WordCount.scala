package com.mystudy.spark.rdd

import org.apache.spark.SparkContext

object WordCount {
  def main(args:Array[String]): Unit = {
    val sc = new SparkContext(master = "local", appName = "WordCount")

    val lines = sc.textFile("data/book.txt")

    val words = lines.flatMap(x=>x.split(" "))

    val wordCount =words.countByValue().toSeq.sortBy(x=>x._2).reverse

    wordCount.foreach(x=>println(f"${x._1} is ${x._2} times"))

    val wordTotal = words.count()
    println("*"*20)
    println("TOTAL")
    println(wordTotal)



  }

}
