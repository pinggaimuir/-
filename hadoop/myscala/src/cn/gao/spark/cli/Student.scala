package cn.gao.spark.cli

/**
  * Created by gao on 2016/12/29.
  */
class Teacher2(val name:String="feng"){
}
class Student1(name:String,val age:Int)extends Teacher2("le"){
  //聚合
//  rdd.aggregate()
//  val rdd1=sc.parallelize(List(("le",1),("yue",2),("gao",3),("le",2),("gao",2)))
//  val rdd2=sc.parallelize(List(("gao",1),("jian",2),("feng",3)))

  //结果：

}
class Student2(override val name:String,val age:Int)extends Teacher2("yue"){

}
object Student{
  def main(args: Array[String]) {
    val s1=new Student1("gao",23)
    val s2=new Student2("jian",24)
    println(s1.name)
    println(s2.name)
  }
}
