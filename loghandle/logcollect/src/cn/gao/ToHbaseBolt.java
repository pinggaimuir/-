package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cn.gao.dao.HbaseDao;
import cn.gao.domain.FluxInfo;

import java.util.Map;

/**
 * Created by gao on 2017/2/3.
 */
public class ToHbaseBolt extends BaseRichBolt {
    OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        FluxInfo fi = new FluxInfo();
        fi.setTime(tuple.getStringByField("time"));
        fi.setUv_id(tuple.getStringByField("uv_id"));
        fi.setSs_id(tuple.getStringByField("ss_id"));
        fi.setSs_time(tuple.getStringByField("ss_time"));
        fi.setUrlname(tuple.getStringByField("urlname"));
        fi.setCip(tuple.getStringByField("cip"));
        HbaseDao.insertData(fi);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
