package cn.gao.before

/**
  * Created by gao on 2016/12/28.
  */
class DataFrameWork
case class ComputationFramework(name:String,popular:Boolean)extends DataFrameWork
case class StorageFramework(name:String,popular:Boolean)extends DataFrameWork
object PatternMatch {
  def main(args:Array[String]): Unit ={
    getSalary("gdsfgf",10)
    getMatchType(100.1f)
    getMatchColl(Array[String]("java","python","cc"))
    getBigDataType(ComputationFramework("gao",true))
    getValue("spark",Map(("spark","gao"),("hadoop","jian")))
  }
  //case class匹配
  def getBigDataType(data:DataFrameWork){
    data match {
      case ComputationFramework(name,popular)=>println("ComputationFramework---name:"+name+",popular:"+popular)
      case StorageFramework(name,popular)=>println("StorageFramework---name:"+name+",popular:"+popular)
      case _=>println("Unkown Type")
    }
  }
  //值匹配
  def getSalary(name:String,age:Int): Unit ={
    name match{
      case "spark"=>println("$150000")
      case "hadoop"=>println("$90000")
      case _ if name=="scala"=>println("$140000")
      case _ if name=="mapreduce"=>println("$70000")
      case _name if age>=15=>println("name:"+_name+",age:"+age+",$20000")
      case _ =>println("$10000")
    }
  }
  //类型匹配
  def getMatchType(msg:Any){
    msg match{
      case i:Int=>println("Integer")
      case s:String=>println("String")
      case d:Double=>println("Double")
      case _ =>println("Ubkown type")
    }
  }
  //集合匹配
  def getMatchColl(msg:Array[String]){
    msg match{
      case Array("scala")=>println("scala")
      case Array("java","python")=>println("java")
      case Array("spark",_*)=>println("spark")
      case _ =>println("Ubkown type")
    }
  }
  //选项匹配
  def getValue(key:String,content:Map[String,String]){
    content.get(key) match {
      case Some(value)=>println(value)
      case None=>println("not fond!")
    }
  }
}
