package com.mystudy.spark.rdd

import org.apache.spark.SparkContext

object tempratures {

  private def parsLines(line : String):(String,String,Float)= {
    val fields = line.split(",")
    val stationID = fields(0)
    val entryType = fields(2)
    val temp = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f
    (stationID,entryType,temp)
  }

  def main(args:Array[String]): Unit = {

    val sc = new SparkContext(master = "local", appName = "temp")

    val lines = sc.textFile("data/1800.csv")

    val parsedlines = lines.map(parsLines)

    val minrdd = parsedlines.filter(x => x._2 == "TMIN")

    val maxrdd = parsedlines.filter(x => x._2 == "TMAX")

    val minStations = minrdd.map(x=>(x._1,x._3.toFloat))

    val maxStations = maxrdd.map(x=>(x._1,x._3.toFloat))

    val minTempByStaions = minStations.reduceByKey((x,y) => math.min(x:Float,y:Float)).sortBy(x=>x._2,ascending = true)

    val maxTempByStaions = maxStations.reduceByKey((x,y) => math.max(x:Float,y:Float)).sortBy(x=>x._2,ascending = false)

    val minResult = minTempByStaions

    val maxResult = maxTempByStaions
    println("MIN")
    minResult.foreach(x=>println(f"${x._1} min temp is ${x._2}"))
    println("*"*30)
    println("MAX")
    println("*"*30)
    maxResult.foreach(x=>println(f"${x._1} max temp is ${x._2}"))

  }

}
