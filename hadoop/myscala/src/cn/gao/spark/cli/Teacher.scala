package cn.gao.spark.cli

/**
  * Created by gao on 2016/12/29.
  */
case class Teacher(var name:String,age:Int){
  def tech(name:String){
    println(s"${name}正在教课")
  }
  def add(age:Int): Int ={
    age+1
  }
}
object Teacher {
  def main(args: Array[String]) {
    val t1=new Teacher("tony",18)
    println(t1.name)
    println(t1.tech(t1.name))
    val t2=new Teacher("lisa",15)
    println(t2.tech(t2.name))

  }
//  def apply(name:String,age:Int): Teacher ={
//    new Teacher(name,age)
//  }
}
