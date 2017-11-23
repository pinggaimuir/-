package cn.gao.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
/**
  * Created by gao on 2017/1/14.
  */
case class Person(name:String,age:Long)
object DataSetCreate {
  def main(args: Array[String]) {
    val session=SparkSession
        .builder()
        .appName("people")
        .getOrCreate()
    //可以将rdd隐式转换为dataFrame
    import session.implicits._
    val peopleDF=session.sparkContext
                .textFile("file:///opt/mydata/people.txt")
                .map(_.split(","))
                .map(attribute=>Person(attribute(0),attribute(1).trim.toInt))
                .toDF()
    //把dataframe注册为临时表,表以case为模式
    peopleDF.createOrReplaceTempView("people")
    //SQL statement
    val teenagerDF=session.sql("select name,age from people where age between 13 and 19")
    //可以通过下标访问
    teenagerDF.map(teenager=>"name:"+teenager(0)).show()
    //也可以通过字段名访问
    teenagerDF.map(teenager=>"Name:"+teenager.getAs[String]("name")).show()
    //为数据集明确定义编码
    implicit val mapEncoder=org.apache.spark.sql.Encoders.kryo[Map[String,Any]]
    implicit val stringIntMapEncoder: Encoder[Map[String, Int]] = ExpressionEncoder()
    //  row.getValuesMap [T]立即检索多个列到Map [String，T]
    teenagerDF.map(teenager=>teenager.getValuesMap[Any](List("name","age"))).collect()
  }
}
