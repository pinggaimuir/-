package cn.gao.before

/**
  * Created by gao on 2016/12/26.
  */
object First {
  def main(args:Array[String]):Unit = {
    var age = 10;
    //当else前后的值类型相同，则result的值与其相同
    var result = if (age > 25) "gao" else "jian"
    //当else两边的类型不一样的时候，result的类型为any
    var result2 = if (age > 18) "adlt" else 1
    //当没有else是相当于 if(age>18) "a" else (),result类型为any
    var result3 = if (age > 18) "a"
    //    println(result3)
    //    var x,y=0
    /*    var result4=if(age<18){
      x=x+1
      y=y+1
      x+y
    }else 0
    var z=if(result4<0){
      println(result4)
    }else{
      println("wo")
    }
    println(z) */
    /*
    * <-为值的提取符，to是一个方法,if是有值的，所以可以放在for等控制结构中用于限制结果
    * */
    //    for(i <- 0 to 5 if i%2==0){
    //      println(i)
    //    }
    //    var flag=true
    //    var sum =0
    //    //输出为15，for循环中加入if叫做条件守卫，用于限制for循环（优化for循环，去掉不必要的执行步骤，或者说跳出for循环）
    //    for(i<-0 to 6 if flag){
    //      sum=sum+i
    //      if(5==i)flag=false
    //    }
    //    println(sum)
    //    import scala.util.control.Breaks._
    //    var flag=true
    //    breakable{
    //      while(flag){
    //        for(item<-"spark"){
    //          println(item)
    //          if(item=='r'){
    //            flag=false
    //            break
    //          }
    //        }
    //      }
    //    }
    //
    //    println("while finished!")


  }
}
