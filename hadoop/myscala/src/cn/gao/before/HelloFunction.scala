package cn.gao.before

/**
  * Created by gao on 2016/12/26.
  */
object HelloFunction {
  def main(args:Array[String]): Unit ={
//    print(hello("spark",13))
    print(sum(1 to 1000: _*))
    println(sumrecursive(1 to 100: _*))

  }

  def hello(name:String,age:Int)={
    println(name)
    println(age)
//    age
    name
  }
  def sum(numbers:Int*)={
    var result =0
    for(number<-numbers)result+=number
    result
  }

  def sumrecursive(numbers:Int*):Int={
    if(0==numbers.length) 0
    else numbers.head + sumrecursive(numbers.tail: _*)
  }
}
