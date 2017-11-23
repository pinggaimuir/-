package cn.gao;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import storm.kafka.*;

import java.util.UUID;

/**
 * Created by gao on 2017/1/30.
 */
public class FluxTopology {
    public static void main(String[] args) {
        //SPOUT的id 要求唯一
        String KAFKA_SPOUT_ID = "flux_spout";
        //要连接的kafka的topic
        String CONSUME_TOPIC = "test";
        //要连接的zookeeper的地址
        String ZK_HOSTS = "192.168.8.151:2181";

        //设定连接服务器的参数
        BrokerHosts hosts = new ZkHosts(ZK_HOSTS);
        SpoutConfig spoutConfig = new SpoutConfig(hosts, CONSUME_TOPIC, "/" +
                CONSUME_TOPIC, UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        //从kafka读取数据发射
        TopologyBuilder builder=new TopologyBuilder();

        builder.setSpout(KAFKA_SPOUT_ID,kafkaSpout);
        //清理数据
//        builder.setBolt("ClearBolt", new ClearBolt()).shuffleGrouping(KAFKA_SPOUT_ID);
//        //计算pv
//        builder.setBolt("PvBolt", new PVBolt()).shuffleGrouping("ClearBolt");
//        //计算uv
//        builder.setBolt("UvBolt", new UvBolt()).shuffleGrouping("PvBolt");
//        //计算vv
//        builder.setBolt("VvBolt", new VvBolt()).shuffleGrouping("UvBolt");
//        //计算newip
//        builder.setBolt("NewIpBolt", new NewIpBolt()).shuffleGrouping("VvBolt");
//        //计算newcust
//        builder.setBolt("NewCustBolt", new NewCustBolt()).shuffleGrouping("NewIpBolt");
//        //将结果数据落地到数据库中
//        builder.setBolt("ToMysqlBolt", new ToMysqlBolt()).shuffleGrouping("NewCustBolt");

        builder.setBolt("PrintBolt", new PrintBolt()).shuffleGrouping(KAFKA_SPOUT_ID);
        //将数据持久化到hbase中间存储中，方便后续使用
//        builder.setBolt("ToHBaseBolt", new ToHbaseBolt()).shuffleGrouping("NewCustBolt");


        StormTopology topology=builder.createTopology();
        Config config=new Config();
        LocalCluster cluster=new LocalCluster();
        cluster.submitTopology("mytopology",config,topology);

        Utils.sleep(1000*1000);
        cluster.killTopology("mytopology");
        cluster.shutdown();
    }
}
