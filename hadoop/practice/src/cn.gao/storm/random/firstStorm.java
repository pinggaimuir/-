package cn.gao.storm.random;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * Created by gao on 2016/12/22.
 */
public class firstStorm {
    public static void main(String[] args) {
        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout("spout",new RandomSpout());
        builder.setBolt("bolt",new SequenceBolt()).shuffleGrouping("spout");
        Config config=new Config();
        if(args!=null&&args.length>0){
            config.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0],config,builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            }

        }else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("firstStorm", config, builder.createTopology());

            Utils.sleep(20000);
            cluster.killTopology("firstStorm");
            cluster.shutdown();
        }
    }
}
