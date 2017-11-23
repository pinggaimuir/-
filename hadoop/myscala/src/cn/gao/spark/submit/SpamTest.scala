package cn.gao.spark.submit

import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gao on 2017/1/6.
  */
object SpamTest {
  def main(args: Array[String]) {
    val conf=new SparkConf()
    conf.setAppName("spam")
    conf.setMaster("local")
    conf.set("spark.shuffle.manager", "hash")
    val sc=new SparkContext(conf)
    val spam=sc.textFile("spam.txt")
    val normal=sc.textFile("normal.txt")

    val tf=new HashingTF(numFeatures = 10000)
    val spamFeatures=spam.map(email=>tf.transform(email.split(" ")))
    val normalFeatures=normal.map(email=>tf.transform(email.split(" ")))
  }
}
