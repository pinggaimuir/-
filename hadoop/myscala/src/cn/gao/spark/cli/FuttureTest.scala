package cn.gao.spark.cli

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by gao on 2016/12/30.
  */
object FuttureTest {
  def main(args: Array[String]) {
    var fu1=Future{
      println("6")
      Thread.sleep(200)
      100
    }
    var fu2=Future{
      println("future2 start ...")
      Thread.sleep(300)
      200
    }
    var c=for(a<-fu1;b<-fu2) yield a+b
    //阻塞等待
    println(Await.result(c,Duration.Inf))
  }
}
