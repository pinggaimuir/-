package cn.gao.spark.cli

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by gao on 2016/12/29.
  * val list1=scala.io.Source.fromFile("C:\\Users\\gao\\Desktop\\aaa.txt").getLines().toList.map(_.split(",")).map(x=>(x,1)).groupBy(x=>x._1).mapValues(x=>x.map())
  */
object Hello1 {
  def main(args: Array[String]) {
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    var fu=Future{
      println("开始计算")
      Thread.sleep(200)
      100
    }
    //持久化,阻塞等待
    val r=Await.result(fu,Duration.Inf)
    print(r)
    //异步
//    fu.onSuccess{
//        //匹配就执行，不匹配就等待
//        case x:Int=>println(x)
//    }
//    Thread.sleep(1000)//触发执行
  }
}
