package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.gao.util.FluxUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by gao on 2017/1/31.
 */
class ClearBolt extends BaseRichBolt {
    private OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String str=tuple.getStringByField("str");
        String[] values=str.split("\\|");
        String uv_id=values[13];
        String ss_id=values[14].split("_")[0];
        String ss_time=values[14].split("_")[0];
        String urlname=values[1];
        String cip=values[15];
        Date date=new Date(Long.parseLong(ss_time));
        String time= FluxUtils.formatDate(date);
        collector.emit(new Values(time,uv_id,ss_id,ss_time,urlname,cip));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("time","uv_id","ss_id","ss_time","urlname","cip"));
    }
}
