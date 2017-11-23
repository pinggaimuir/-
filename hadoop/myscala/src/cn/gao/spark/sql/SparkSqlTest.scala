package cn.gao.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SparkSession
/**
  * Created by gao on 2017/1/13.
  */
object SparkSqlTest {
  def main(args: Array[String]) {
    val sc=new SparkContext()
    val hiveCtx=new HiveContext(sc)

    // 如果您有现有的Hive安装，并且已将hive-site.xml文件复制到$ SPARK_HOME / conf，
    // 你也可以只运行hiveCtx.sql来查询你现有的Hive表
    val schemaRdd=hiveCtx.read.json("inputpath")
    val result=hiveCtx.sql("select text,retweetcount from tweet order by text limit 10")

  }
}
