package cn.gao.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created by gao on 2016/12/22.
 */
public class StormMain {
    private static final String SENTENCE_SPOUT_ID = "sentence-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String COUNT_BOLT_ID = "count-bolt";
    private static final String REPORT_BOLT_ID = "report-bolt";
    private static final String TOPOLOGY_NAME = "word-count-topology";

    public static void main(String[] args) throws InterruptedException {
        SentenceSpout spout=new SentenceSpout();
        SpiltBolt spiltBolt=new SpiltBolt();
        WordCountBolt wordCountBolt=new WordCountBolt();
        ReportBolt reportBolt=new ReportBolt();

        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout(SENTENCE_SPOUT_ID,spout);
        builder.setBolt(SPLIT_BOLT_ID,spiltBolt).shuffleGrouping(SENTENCE_SPOUT_ID);
        builder.setBolt(COUNT_BOLT_ID,wordCountBolt).fieldsGrouping(SPLIT_BOLT_ID,new Fields("word"));
        builder.setBolt(REPORT_BOLT_ID,reportBolt).globalGrouping(COUNT_BOLT_ID);

        Config config=new Config();
//        StormSubmitter.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());
        LocalCluster cluster=new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());

        Thread.sleep(10000);
        cluster.killTopology(TOPOLOGY_NAME);
        cluster.shutdown();

    }
}
