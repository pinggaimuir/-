package cn.gao.before

/**
  * Created by gao on 2016/12/28.
  */
object FunctionPro {
  def main(args:Array[String]): Unit ={
    //函数可以直接赋值给变量
    val hiData=bigData _
    println(hiData("spark"))
    //匿名函数，只需要函数参数和函数体，不要名称，一般吧匿名函数赋值给变量（其实是val常量）
    val hiBig=(name:String)=>println("hi,"+name)
    println(hiBig("kafka"))
    //直接将函数作为参数传递给函数
    //在java中是new一个接口实例，在借口实例的回调方法calback中来实现业务逻辑
    def getName(func:(String)=>Unit,name:String): Unit ={
      func(name)
    }
    getName(hiBig,"Scala")

    val mularray=Array(1 to 10: _*).map((item:Int)=>item*2).foreach(x=>println(x))
    //当函数的返回类型是函数的时候，这个时候就表明了scala的函数实现了闭包，
    //闭包原理：scala函数后面是列和对象，所以，scala的参数都作为了对象的成员！！！！，所以可以后续访问
    def funResult(message:String)=(name:String)=>println(message+":"+name)
    funResult("hello")("java")//Curring函数写法
    //上面的调用等同于下面两句
    val result=funResult("hello")
    result("java")

    //交叉字符集
    val x="hello".intersect("word")
    println(x)




  }
  def bigData(name:String): Unit ={
    println("hi,"+name)
  }

}
