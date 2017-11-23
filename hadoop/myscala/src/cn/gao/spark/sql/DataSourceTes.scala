package cn.gao.spark.sql

import org.apache.spark.sql.SparkSession

/**
  * Created by gao on 2017/1/14.
  */
object DataSourceTes {
  def main(args:Array[String]): Unit ={
    val session=SparkSession.builder().getOrCreate()
    import session.implicits._
    val userDf=session.read.load("file:///opt/mydata/user.parquet")
    userDf.select("name","age").write.save("file:///opt/mydata/user.parquet")

    val peopleDf=session.read.format("json").load("file:///opt/mydata/user.json")
    peopleDf.select("name","age").write.format("parquet").save("namesAndAges.parquet")

    val sqlDF = session.sql("SELECT * FROM parquet.`users.parquet`")
  }
}
