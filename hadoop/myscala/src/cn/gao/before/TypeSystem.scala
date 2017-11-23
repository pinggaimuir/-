package cn.gao.before

/**
  *  1 scala的类和方法、函数、都可以是泛型，
  *  2 View Bounds，可以吧你的类型在没有知觉的情况下转换为目标类型，可以认为是上边界和下边界的加强补充版本 ,
  *    例如在SparkContext中有T <%Writbale方式的代码，表示T必须是Wriabtle类型的，但是T没有直接继承自Writbale接口，
  *    此时就要通过“implicit"的方式来实现这个功能。
  * Created by gao on 2016/12/28.
  */


object TypeSystem {
  def main(args: Array[String]) {
    //    val p=new Person("scala")
    //    val w=new Worker("Spark")
    //    val d=new Dog("dahuang")
    //    new Club(p,w).comunicate
    //    new Club(p,d).comunicate
    //  }
    //  class Animal[T](val species:T){
    //    def getAnimal(specie:T):T = species
    //  }
    //
    //  class Person(val name:String){
    //    def talk(person:Person){
    //      println(this.name+":"+person.name)
    //    }
    //  }
    //  class Worker(name:String)extends Person(name)
    //
    //  class Dog(val name:String)
    //  implicit def dog2Person(dog:Dog)=new Person(dog.name)
    //
    //  //上边界表达了一个泛型类型必须是某种类型的子类，语法<:，对类型进行限定；下边界为>:
    //  class Club[T<% Person](p1:T,p2:T){
    //    def comunicate=p1.talk(p2)
    //  }
  }
}
