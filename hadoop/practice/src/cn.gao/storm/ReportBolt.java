package cn.gao.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gao on 2016/12/22.
 */
public class ReportBolt extends BaseRichBolt {
    Map<String,Integer> map=new HashMap<String, Integer>();
    OutputCollector collector=null;
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    public void execute(Tuple tuple) {
        try {
            String word = tuple.getStringByField("words");
            int count = tuple.getIntegerByField("count");
//            System.out.println("单词" + word + "出现" + count + "次");
            System.out.println("################单词"+word+"已经出现了"+count+"次");
            map.put(word,count);
            collector.ack(tuple);
        }catch (Exception e){
            collector.fail(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public void cleanup() {
        System.out.println("aaaa——————————————————————————————————————————————————");
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            System.out.println("单词"+entry.getKey()+"出现了---------"+entry.getValue());
        }
        System.out.println("bbbb——————————————————————————————————————————————————");
    }
}
