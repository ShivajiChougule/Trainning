package com.mystudy.spark.dataframeanddataset

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, size, split, sum}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object mostObscureHero {
  private case class SuperHeroNames(id: Int, name: String)

  private case class SuperHero(value: String)


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("superHero").getOrCreate()

    val superHeroNamesSchema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)

    import spark.implicits._

    val names = spark.read
      .schema(superHeroNamesSchema)
      .option("sep", " ")
      .csv("data/Marvel-names.txt")
      .as[SuperHeroNames]

    val lines = spark.read
      .text("data/Marvel-graph.txt")
      .as[SuperHero]

    val connections = lines
      .withColumn("id", split(col("value"), " ")(0))
      .withColumn("connections", size(split(col("value"), " ")) - 1)
      .groupBy("id").agg(sum("connections").alias("connections"))
      .sort($"connections".asc)

    val lessPopular = connections
      .filter($"connections"<=1)

    val obscure = lessPopular.join(names,usingColumn = "id")

    obscure.show()

  }

}
