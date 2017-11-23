package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.gao.dao.HbaseDao;
import cn.gao.domain.FluxInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by gao on 2017/2/3.
 */
public class NewIpBolt extends BaseRichBolt {
    OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        List<Object> values=tuple.getValues();
        //如果uv_id在今天的其他数据中没有出现过，则输出1，否则输出0
        List<FluxInfo> list= HbaseDao.queryData("^[^_]*_[^_]*_[^_]*_"+tuple.getStringByField("cip")+"_.*$");
        values.add(list.size()==0?1:0);
        collector.emit(new Values(values.toArray()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("time","uv_id","ss_id","ss_time","urlname","cip","pv","uv","vv","newip"));
    }
}
