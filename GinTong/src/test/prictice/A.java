package test.prictice;

/**
 * Created by gao on 2017/3/1.
 */
public class A {
    private int num=100;
    public void counterOne(B b){
        int num=101;
        countTwo(num);
        System.out.println("num1="+num+"");
        b.add(num);
    }
    public void countTwo(int num){
        num=this.num;
        System.out.println("num2="+this.num+"");
    }
    public static void main(String[] args) {
        A a=new A();
        B b=a.new B();
        a.counterOne(b);
        b.print();
    }

    private class B{
        private  int num=100;
        public B add(int n){
            num=num++ +n;
            return this;
        }
        public void print(){
            System.out.println("num3="+num+"");
        }
    }
}
