package cn.gao.before

/**
  * Created by gao on 2016/12/28.
  */
object Parttern {
  def main(args: Array[String]) {
    var x=rsum(1 to 5:_*)
    println(x)


  }
  def dec(a:String,left:String="[",right:String="]"):String=left+a+right
  def sum(args:Int*) ={
    var r=0
    for(i<-args){
      r+=i
    }
    r
  }
  def rsum(args:Int*):Int={
    if(args.length==0)0
    else args.head + rsum(args.tail:_*)
  }
}
