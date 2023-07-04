package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{FloatType, IntegerType, StructType}
import org.apache.spark.sql.functions._
object totalSpentByCustomer {

  private case class customer(id:Int, any:Int, amount:Float)

  def main(args:Array[String]):Unit={
    val spark = SparkSession.builder().master("local").appName("customerTotal").getOrCreate()

    val custShema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("any", IntegerType, nullable = true)
      .add("amount", FloatType, nullable = true)


    import spark.implicits._

    val fields = spark.read
      .schema(custShema)
      .csv("data/customer-orders.csv")
      .as[customer]

    val customers = fields.select("id","amount")

    val totalAmounts = customers.groupBy("id").agg(round(sum("amount"),2))
      .withColumnRenamed("round(sum(amount), 2)","total").sort("id")

    totalAmounts.show(totalAmounts.count.toInt)


  }

}
