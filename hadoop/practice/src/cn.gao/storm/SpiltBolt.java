package cn.gao.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by gao on 2016/12/22.
 */
public class SpiltBolt extends BaseRichBolt{
    @Override
    public void cleanup() {
        super.cleanup();
    }

    private OutputCollector outputCollector=null;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector=outputCollector;
    }
    private int count=0;
    public void execute(Tuple tuple) {
        try {
            String sentence = tuple.getStringByField("sentence");
            if("i am behend wu zhi shan".equals(sentence) && count < 3){
                count ++;
                throw new RuntimeException("发生错误，发送不出去~~~~");
            }
            String words[] = sentence.split(" ");
            for (String word : words) {
                outputCollector.emit(tuple, new Values(word));
            }
            outputCollector.ack(tuple);
        }catch(Exception e){
            e.printStackTrace();
            outputCollector.fail(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}
