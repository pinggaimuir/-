package cn.gao.spark.submit

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by gao on 2017/1/3.
  */
object TestJob {
  def main(args: Array[String]) {
    val config = new SparkConf()
    //必须和生成和jar文件一致
    config.setAppName("first")
        config.setMaster("local")
//    config.setMaster("spark://spark01:7077") //集群
    config.set("spark.shuffle.manager", "hash")

    var sc = new SparkContext(config)
    val r = sc.textFile("file:///opt/mydata/letter.txt", 2)
      .map { x => {
        val a = x.split(",");
        (a(0), a(1))
      }
      }
      .map(x => (x._1, x._2 + "-s"))
    //数据落地，本地磁盘，result是一个前缀，每一个分区是一个文件，文件名在写入前应该不存在
    r.saveAsTextFile("file:///opt/mydata/result")
  }
}
