package cn.gao.spark.me

import java.sql.{ResultSet, DriverManager}

import com.fasterxml.jackson.databind.ObjectMapper
//import org.apache.hadoop.hbase.HBaseConfiguration
//import org.apache.hadoop.hbase.io.ImmutableBytesWritable
//import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.io.{MapWritable, LongWritable, IntWritable}
import org.apache.hadoop.mapred.KeyValueTextInputFormat
import org.apache.spark.{Partitioner, HashPartitioner, SparkContext}
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.codehaus.janino.Java



/**
  * Created by gao on 2016/12/28.
  */
class WCspark(name:String){

}
object WCspark {
//  val sc=new SparkContext()
  def main(args: Array[String]) {
    hello("hello")
    printMe("gao")
  }
  implicit def str2WCspark(title:String):WCspark=new WCspark(title)
  def hello(w:WCspark): Unit ={
    println("hello")
  }
  def printMe(str:String): Unit ={
    println(str)
  }
}
