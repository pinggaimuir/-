package cn.gao.before

/**
  * Created by gao on 2016/12/27.
  * scala中数组创建操作：
  *   1 最原始数组创建方式：val array=new Array[Int](5),指定数组长度为固定5个长度
  *   2 最常用和经典访问的创建数组的方式形如Array[Int](1,2,3,4,5),直接通过Array类名并传入参数，这种方式的数组长度是不可变的，在背后的实现是调用Array的
  *     工厂方法模式apply来构建数组和数组内容的
  *   3 对数组元素访问的时候下标范围在0到length-1长度，超过会包异常
  *   4 如果想使用可变数组要导入scala.collection.mutable.ArrayBuffer,用ArrayBuffer
  *   5 ArrayBuffer增加元素默认是在末尾增加元素
  */
object collecttest {
  def main(args:Array[String]):Unit={
    //传统方式
    val array1=new Array[Int](5)
    //通过类名+参数，可以去掉泛型是cala有类型推倒能力，可以根据值推倒出类型，长度不可变
    val array2=Array[Int](1,2,3)
    //同上，调用类的工厂方法
    val array=Array.apply(1,2,7,6,3,4,5)
//    for(item<-array)println(item)

    val names=Array("Scala","kafka","hadoop",3)
    import scala.collection.mutable.ArrayBuffer
    val arrayBuffer=ArrayBuffer[Int]()
    arrayBuffer+=1
    arrayBuffer+=2
    arrayBuffer+=3
    arrayBuffer+=(4,5,6,7,8)
    //用两个++=来在末尾添加数组
    arrayBuffer++=Array(10,11,12)
    //在指定位置插入元素
    arrayBuffer.insert(3,100,1000,10000)
    //删除元素
    arrayBuffer.remove(arrayBuffer.length-1)
    //当需要多线程并发时，把arrayBuffer转换成Array，因为Array一般不可变
    arrayBuffer.toArray
    for(item<-arrayBuffer)println(item)

    for(i<-0 until array.length)print(array(i)+" ")
    //每次打印下标+2个step才打印
    for(i<-0 until (array.length,2))print(array(i)+" ")
    //翻转下标，打印
    for(i<-(0 until array.length).reverse)print(array(i)+" ")
    //求和
    print("sum:"+array.sum)
    //对数组进行升序排序
    scala.util.Sorting.quickSort(array)
    //打印结果为：quiksort1:1,2,3,4,5,6,7
    print("quiksort1:"+array.mkString(","))
    //打印结果为：quiksort2:*****1,2,3,4,5,6,7*****
    print("quiksort2:"+array.mkString("*****",",","*****"))
    //sparkmap函数的原型，用yield产生新的元素
    val arrayAddedOne=for(item<-array) yield item +1
    println(arrayAddedOne.mkString(" "))
    //添加守卫
    val arrayEven= for(item<-array if item%2==0)yield item+1
    println(arrayEven.mkString(" "))
    println(array.filter(x=>x%2==0).mkString(" "))
    //如果针对所欲元素可以用_取代
    println(array.filter( _%2==0).mkString(" "))
    //map是便利集合中的所有元素，对每个元素做处理
    println(array.filter( _%2==0).map(_*10).mkString(" "))
  }
}
