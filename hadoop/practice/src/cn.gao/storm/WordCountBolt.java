package cn.gao.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gao on 2016/12/22.
 */
public class WordCountBolt extends BaseRichBolt {
    private Map<String,Integer> map=new HashMap<String, Integer>();
    OutputCollector collector=null;
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    public void execute(Tuple tuple) {
        try {
            String word = tuple.getStringByField("word");
            map.put(word, map.containsKey(word) ? map.get(word) + 1 : 1);
            collector.emit(tuple, new Values(word, map.get(word)));
            collector.ack(tuple);
        }catch (Exception e){
            collector.fail(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("words","count"));
    }
}
