package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.List;
import java.util.Map;

/**
 * Created by gao on 2017/1/31.
 */
public class PVBolt extends BaseRichBolt {
    private OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        List<Object> values=tuple.getValues();
        values.add(1);
        collector.emit(new Values(values.toArray()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("time","uv_id","ss_id","ss_time","urlname","cip","pv"));
    }
}
