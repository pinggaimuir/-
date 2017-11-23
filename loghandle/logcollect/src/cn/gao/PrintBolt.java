package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by gao on 2017/2/3.
 */
public class PrintBolt extends BaseRichBolt {
    private OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
//        Fields fields =tuple.getFields();
//        StringBuffer buf=new StringBuffer();
//        Iterator<String> it=fields.iterator();
//        while(it.hasNext()){
//            String key=it.next();
//            Object value=tuple.getValueByField(key);
//            buf.append("--------"+key+":"+value+"----------------");
//        }
//        System.out.println(buf.toString());
//        collector.emit(tuple.getValues());
        String value=tuple.getStringByField("str");
        System.out.println("value-------------"+value);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("time","uv_id","ss_id","ss_time","urlname","cip"));
    }
}
