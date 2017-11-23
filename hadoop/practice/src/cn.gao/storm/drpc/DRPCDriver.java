package cn.gao.storm.drpc;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.daemon.drpc;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

/**
 * Created by gao on 2016/12/23.
 */
public class DRPCDriver {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //--指定一个被调用方法的名字
        LinearDRPCTopologyBuilder builder=new LinearDRPCTopologyBuilder("exec");
        //--设置要被调用topology中的执行bolt，可以设置多个
        builder.addBolt(new ExclaimBolt());
        Config config=new Config();
        //提交drpcTopology到服务器
        StormSubmitter.submitTopology("drpc_topo",config,builder.createRemoteTopology());
        //本地模拟远程调用
//        LocalDRPC localDRPC=new LocalDRPC();
//        LocalCluster cluster=new LocalCluster();
//        cluster.submitTopology("drpc_topology",config,builder.createLocalTopology(localDRPC));
//        String result=localDRPC.execute("exec","hello");
//        System.out.println("__________________----------------------result"+result);
//        cluster.shutdown();
//        localDRPC.shutdown();
    }
}
