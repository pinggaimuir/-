package cn.gao.storm.random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * Created by gao on 2016/12/22.
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector=null;
    private static String words[]={"hadoop","storm","hbase","hive","linux","scala","spark"};

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("randomstring"));
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector=spoutOutputCollector;
    }

    public void nextTuple() {
        String word= words[new Random().nextInt(words.length)];
        collector.emit(new Values(word));
    }
}
