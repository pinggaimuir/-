package cn.gao.before

/**
  * Created by gao on 2016/12/27.
  * 1 在定义Scala的class的时候可以直接在类名后面（）里加入类的构造参数，此时在apply方法中页必须有这些参数
  * 2 在Scala的object中可以有多个apply
  * 2 如果名称相同，则object中的内容都是class的静态内容，也就是说object中的内容class都可以在没有实例的时候直接调用
  *  正是因为可以在没有类的实例的时候去调用object中的一切内容，所以可以使用object中特定方法来创建类的实例，而这个特定的
  *  方法就是apply方法。
  * 3 object 中的apply方式class对象生成的工厂方法，用于控制对象的生成。
  * 4 很多框架的代码一般直接调用抽象类的object的apply方法生成类的实例对象：
  *     第一：其秘诀在于apply具有类对象生成的一切生杀大权，首相类不可以实例化，在apply中可以实例化抽象类的子类，以
  *     Spark的图计算为例，Graph的抽象的class，在object Graph中的apply方法实际上调用了Graph的子类GraphImpl来构建
  *     Graph类型的对象实例的，当然从Spark图计算的源码可以看出，GraphImpl的构造业使用了object GraphImpl的apply方法
  *     第二：这种方式神奇的效应在于更加能够应对版本迭代或者修改的变化，这是更高意义的面向接口编程；
  */
//是object HellOOP的伴生类，可以直接访问object HellOOP的一切内容
class HelloOOP(age:Int,name:String){
//  var name="Spark"
  def sayHello=println("Hi,My name is "+name+" my name is "+age)
}
//是class HelloOOP的伴生对象，可以直接访问class HelloOOP的一切内容，private[this]是特例
object HelloOOP {
  var number=0
  def main(args:Array[String]):Unit={
    println("Hello Scala OOp!")
    val helloOOP=HelloOOP(18)
    helloOOP.sayHello


  }
  def apply(age:Int):HelloOOP={
    println("my number is :"+number)
    number+=1
    new HelloOOP(age,"")
  }
  def apply(age:Int,name:String):HelloOOP={
    new HelloOOP(age,name)
  }
}



