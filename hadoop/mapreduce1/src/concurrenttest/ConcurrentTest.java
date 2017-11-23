//package concurrenttest;
//
//import io.netty.util.Timeout;
//
//import java.util.concurrent.*;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by gao on 2017/1/10.
// */
//public class ConcurrentTest {
//    public static void main(String[] args) throws InterruptedException, ExecutionException {
//      CountDownLatch latch=new CountDownLatch(2);
//        ExecutorService pool=new ThreadPoolExecutor(5,10,3000, TimeUnit.SECONDS,
//                new LinkedBlockingDeque<Runnable>(5),new RejectedExecutionHandler(){
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                System.out.println("你先等会！");
//            }
//        });
//        ExecutorService pool2=Executors.newFixedThreadPool(2);
//        ReentrantLock lock=new ReentrantLock();
//        if(lock.tryLock()){
//            lock.unlock();
//        }else{
//
//        }
//    }
//}
//class Run1 implements Runnable{
//    CountDownLatch latch=null;
//    public Run1(CountDownLatch latch){
//        this.latch=latch;
//    }
//    @Override
//    public void run() {
//        System.out.println(" yi qi?");
//        latch.countDown();
//    }
//}
//class Run2 implements Runnable{
//    CountDownLatch latch=null;
//    public Run2(CountDownLatch latch){
//        this.latch=latch;
//    }
//    @Override
//    public void run() {
//        System.out.println("hao a ?");
//        latch.countDown();
//    }
//}
//class Call1 implements Callable{
//
//    @Override
//    public String call() throws Exception {
//        return "执行完毕";
//    }
//}
