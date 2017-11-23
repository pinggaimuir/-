package cn.gao.storm.drpc;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * 在执行DRPC的过程中，execute方法接受的tuple中具有n+1个值，
 * 第一个为request-id即请求的编号，后n个字段是请求的参数
    同时要求我们topology的最后一个bolt发送一个形如[id, result]的二维tuple：
    第一个field是request-id，第二个field是这个函数的结果。最后所有中间tuple的第一个field必须是request-id。
 * Created by gao on 2016/12/23.
 */
public class ExclaimBolt extends BaseRichBolt {
    OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }
    @Override
    public void execute(Tuple tuple) {
        String str=tuple.getString(1);
        collector.emit(new Values(tuple.getValue(0),tuple+"!"));

    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("id","result"));
    }
}
