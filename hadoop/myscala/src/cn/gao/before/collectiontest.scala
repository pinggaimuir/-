package cn.gao.before

/**
  * Created by gao on 2016/12/28.
  */
object collectiontest {
  def main(args:Array[String]): Unit ={
    val range=1 to 10
    val list=List(1,2,3,4,5)
    println(list.head)
    //创建一个linkedList
    var linkedList=scala.collection.mutable.LinkedList(1,2,3,4,5)
    println(linkedList.tail)
    while(linkedList!=Nil){
      //每日次取出第一个元素
      println(linkedList.elem)
      //将剩余的元素赋给linkedlist
      linkedList=linkedList.tail
    }
    //添加元素
    var ll=linkedList.+:(9)
    println(ll)
  }

}
