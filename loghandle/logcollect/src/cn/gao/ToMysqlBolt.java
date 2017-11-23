package cn.gao;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cn.gao.dao.MysqlDao;
import cn.gao.domain.ResultInfo;
import cn.gao.util.FluxUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by gao on 2017/2/3.
 */
public class ToMysqlBolt extends BaseRichBolt {
    OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        ResultInfo ri=new ResultInfo();
        String time=tuple.getStringByField("time");
        Date date = FluxUtils.parseDateString(time);
        ri.setTime(new java.sql.Date(date.getTime()));
        ri.setPv(tuple.getIntegerByField("pv"));
        ri.setUv(tuple.getIntegerByField("uv"));
        ri.setVv(tuple.getIntegerByField("vv"));
        ri.setNewip(tuple.getIntegerByField("newip"));
        ri.setNewcust(tuple.getIntegerByField("newcust"));

        MysqlDao.insert(ri);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
