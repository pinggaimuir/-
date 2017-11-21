package queue.queuetest;

import java.util.concurrent.Exchanger;

/**
 * 交换机用于两个线程之间的信息交互
 * 案例： 连个间谍相互交换暗号
 *  用连个线程代表连个间谍
 * Created by gao on 2016/11/29.
 */
public class ExchangerTestDemo {
    public static void main(String[] args) {
        Exchanger exchanger=new Exchanger();
        new Thread(new Spy1(exchanger)).start();
        new Thread(new Spy2(exchanger)).start();
    }
}
class Spy1 implements Runnable{
    private Exchanger exchanger;
    public Spy1(Exchanger exchanger) {
        this.exchanger=exchanger;
    }

    public void run() {
        String info="天王盖地虎";
        try {

            String Sp2Info= (String) exchanger.exchange(info);
            System.out.println("间谍1收到间谍2的："+Sp2Info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Spy2 implements Runnable{
    private Exchanger exchanger;
    public Spy2(Exchanger exchanger) {
        this.exchanger=exchanger;
    }
    public void run() {
        String info="小鸡炖蘑菇";
        try {
            String Spy1Info= (String) exchanger.exchange(info);
            System.out.println("间谍2收到间谍1的："+Spy1Info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}