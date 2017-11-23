package cn.gao.before

/**
  * Created by gao on 2016/12/27.
  */
trait Logger{
  def log(message:String): Unit ={
    println("Logger:"+message)
  }
}
trait RichLogger extends Logger{
  override def log(message:String): Unit ={
    println("RichLogger:"+message)
  }
}
class Login(val name:String) extends Logger{
  def loggin: Unit ={
    println("Hi ,whlcome!"+name)
    log(name)
  }
}
trait Information{
  def getInfomation:String
  def checkIn:Boolean={
    getInfomation.equals("Spark")
  }
}
class Passenger (val name:String)extends Information{
  def getInfomation=name
}
object TraitTest {
  def main(args:Array[String]): Unit ={
    val person=new Login("Sprak") with RichLogger
    person.loggin
  }
}
