package cn.gao.spark.sql

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

/**
  * Created by gao on 2017/1/14.
  */
object Rdd2DataFrame {
  def main(args:Array[String]): Unit ={
    val session=SparkSession
      .builder()
      .getOrCreate()
    import session.implicits._
    //创建一个rdd
    val peopleRdd=session.sparkContext.textFile("file:///opt/mydata/people.txt")
    //schema用字符串编码
    val schemaString = "name age"
    //根据字符串生成模式
    val fields=schemaString.split(" ")
      .map(fieldName=>StructField(fieldName,StringType,nullable = true))
    val schema=StructType(fields)
    //把rdd的记录转换成rows
    val rowRdd=peopleRdd
      .map(_.split(","))
      .map(attributes=>Row(attributes(0),attributes(1).trim))
    //用schema创建dataFrame
    val peopleDF=session.createDataFrame(rowRdd,schema)
    //创建临时表
    peopleDF.createOrReplaceTempView("people")
    val result=session.sql("select * from people")

    result.map(attributes=>"Name:"+attributes(0)+",age:"+attributes(1)).show()
  }
}
