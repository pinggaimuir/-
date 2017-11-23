package cn.gao.spark.submit

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.Seconds
/**
  * Created by gao on 2017/1/5.
  */
object StreamingTest {
  def main(args: Array[String]) {
    val conf=new SparkConf()
    //必须和生成和jar文件一致
    conf.setAppName("stream1")
    conf.setMaster("local")
    //    config.setMaster("spark://spark01:7077") //集群
    conf.set("spark.shuffle.manager", "hash")
    val ssc=new StreamingContext(conf,Seconds(1))
    val lines=ssc.socketTextStream("hadoop",7777)
    val errorLines=lines.filter(_.contains("error"))
    errorLines.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
