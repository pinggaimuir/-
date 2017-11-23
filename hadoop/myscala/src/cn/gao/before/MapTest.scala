package cn.gao.before

import scala.collection.mutable

/**
  * tuple中可以有很多个
  *
  * Created by gao on 2016/12/27.
  */
class MapTest{
  def say(): Unit ={
    println ("my nmae is gao")
  }
}
class MapTest2 extends MapTest{
  override def say(): Unit ={
    println("my name is jian")
  }
}
object MapTest {
 def main(args:Array[String]):Unit={
   //默认map构造不可变的集合，里边的内容是不可修改的，一旦修改就变成了新的map。原来的map内容保持不变。
    val bigDatas=Map("Spark"->6,"Hadoop"->11)

   val programingLanguage=scala.collection.mutable.Map("Scala"->13,"java"->23)
   programingLanguage("Scala")=14
   for((name,age) <-programingLanguage)println(name+"-----"+age)

   val persons=Map(("jialin",30),("gao",23))
   //查询map中的值，一定要用getOrElse，这样在key不存在的情况下不报异常，而且可以提供默认值
   println(programingLanguage.getOrElse("python","jialin"))

   //如果想直接new出Map实例，则需要使用HashMap等具体的map子类
   val personsInfomation=new mutable.HashMap[String,Int]
    personsInfomation+=("scala"->12,"java"->25)
   personsInfomation-=("java")
   for((name,age)<-personsInfomation)println(name+":"+age)
   //遍历值
   for(name<-personsInfomation.values)println(name)
   //利用yield反转键和值
   val result=for((name,value)<-personsInfomation) yield (value,name)
   for((name,value)<-result)println(name+":"+value)
   //排序map
   val big=scala.collection.immutable.SortedMap(("java",12),("gao",23),("spark",4))
   for((name,age)<-big)println(name+":"+age)
   //linkedHashMap可以记住数据插入的顺序
   val big2=scala.collection.mutable.LinkedHashMap(("java",12),("gao",23),("spark",4))
   for((name,age)<-big2)println(name+":"+age)
    //tuple中可以有多种不同的数据
   val tuple1=("gao","jian","daxiong ","fengfeng",30)
   println(tuple1._4)
 }

}
