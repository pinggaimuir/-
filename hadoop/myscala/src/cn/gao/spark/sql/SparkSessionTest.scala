package cn.gao.spark.sql

import org.apache.spark.sql.SparkSession
/**
  * Created by gao on 2017/1/14.
  */
object SparkSessionTest {
  def main(args: Array[String]) {
    val session=SparkSession
        .builder()
        .appName("Spark Sql Example")
        .config("spark.some.config.option","value")
        .getOrCreate()
    //隐式转换，例如将rdd转换为dataFrame
    import session.implicits._
    val df=session.read.json("filepath")
    df.select($"name", $"age" + 1).show()
    df.createOrReplaceTempView("people")
    df.createTempView("people")
    val sqlDF=session.sql("select * from people")
    sqlDF.show()



  }
}
