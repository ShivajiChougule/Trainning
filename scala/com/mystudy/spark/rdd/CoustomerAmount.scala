package com.mystudy.spark.rdd

import org.apache.spark.SparkContext

object CoustomerAmount {

  def parseLines(line: String): (Int, Float) = {
    val fields = line.split(",")
    val id = fields(0).toInt
    val amount = fields(2).toFloat
    (id,amount)
  }

  def main(args:Array[String]): Unit = {

    val sc = new SparkContext(master = "local", appName = "CustomerAmount")

    val lines = sc.textFile("data/customer-orders.csv")

    val records = lines.map(parseLines)

    val totalamount = records.reduceByKey((x,y)=>x+y).sortBy(x=>x._2,ascending = false).collect()

    totalamount.foreach(x=>println(f"For id ${x._1} amount is ${x._2}"))



  }

}
