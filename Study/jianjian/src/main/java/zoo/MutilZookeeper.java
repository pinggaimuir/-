package zoo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gao on 2016/12/2.
 */
public class MutilZookeeper {
    private ZooKeeper zk;
    //初始化
    @Before
    public void initZk() throws IOException, InterruptedException {
        final CountDownLatch latch=new CountDownLatch(1);
         zk=new ZooKeeper("192.168.8.101:2181,192.168.8.102:2181,192.168.8.103:2181", 3000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("链接成功！");
                    latch.countDown();
                }
            }
        });
        latch.await();
    }
    //数据监听
    @Test
    public void testWatch() throws KeeperException, InterruptedException {
        for(;;){
            final CountDownLatch latch=new CountDownLatch(1);
            zk.getData("/park02", new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
                        System.out.println("数据发生变化！");
                        latch.countDown();
                    }
                }
            },null);
            latch.await();
        }
    }

    /**
     * 监听子节点的创建或者删除，用getChildren
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testChildrenWatch() throws KeeperException, InterruptedException {
        for(;;){
            final CountDownLatch latch=new CountDownLatch(1);
            zk.getChildren("/park02", new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
                        System.out.println("子节点发生变化！");
                        latch.countDown();
                    }
                }
            },null);
            latch.await();
        }
    }
    //如果路径存在指定存在，返回此节点的基点信息，并封装发哦stat中
    //如果不存在，返回null
    @Test
    public void testCreated_Delete() throws KeeperException, InterruptedException {

        for(;;){
            final CountDownLatch latch=new CountDownLatch(1);
            //如果要监听某个节点的创建或者删除事件，应该调用exiists里的watcher
            Stat stat=zk.exists("/park02", new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType()== Event.EventType.NodeCreated){
                        System.out.println("节点被创建了");
                        latch.countDown();
                    }

                    if(watchedEvent.getType()== Event.EventType.NodeCreated){
                        System.out.println("节点被删除了");
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }
    }

    /**
     * 利用回调接口拿到创建的实际路径接口
     * 应用场景：如果创建的xxx顺序节点，这个节点我们需要想办法拿到
     */
    @Test
    public void testCreateStringCallBack(){
        zk.create("/park03", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new AsyncCallback.StringCallback() {
            //i 状态吗，成功返回0，不成功返回-110
            //s 用户指定的路径
            //o 构造函数里传递的数据
            //s1 实际创建的路径节点名称
            public void processResult(int i, String s, Object o, String s1) {
                System.out.println(i+"----"+s+"--------"+o+"---------"+s1);
            }
        },"hello");
        while(true);
    }

}
