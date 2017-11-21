package zoo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gao on 2016/12/4.
 */
public class MyWather implements Watcher {
    private static CountDownLatch latch=new CountDownLatch(1);
    private static ZooKeeper zk;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
         zk=new ZooKeeper("192.168.8.160:2181",3000,new MyWather());
        System.out.println(zk.getState());
        latch.await();
//        zk.create("/park3","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        zk.create("/park3/c1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
//        List<String> childrenList=zk.getChildren("/park3",true);
//        System.out.println(childrenList);
//        zk.create("/park3/c2","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        String path="/park5";
        zk.create(path,"gao".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        zk.getData(path,true,null);
        Stat stat=zk.setData(path,"jian".getBytes(),-1);
        zk.setData(path,"jianjian".getBytes(),stat.getVersion());
        try {
            zk.setData(path, "jianjianjian".getBytes(), stat.getVersion());
        }catch (Exception e){
            System.out.println("duplicated!");
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watchd event:"+watchedEvent);
        if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
            if(Event.EventType.None==watchedEvent.getType()&&null==watchedEvent.getPath()){
                latch.countDown();
            }else if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println(zk.getChildren(watchedEvent.getPath(),true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
class IStringCallBack implements AsyncCallback.StringCallback{
    //创建节点完后，zook客户端会自动调用这个方法，可以处理相关业务
    //rc 状态吗，成功返回0，不成功返回-110
    //path 用户指定的路径
    //ctx 构造函数里传递的数据
    //name 实际创建的路径节点名称
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("crate path result"+rc+"==="+path+"==="+ctx+"==="+name);
    }

}
class IStatCallBack implements AsyncCallback.StatCallback{

    public void processResult(int i, String s, Object o, Stat stat) {
        if(i==0){
            System.out.println("SUCCESS");
        }
    }
}