package cn.gao.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by gao on 2016/12/22.
 */
public class SentenceSpout extends BaseRichSpout{
    private String[] data={"i am wu kong",
                            "500 years ago",
                            "i am da nao tian gong",
                            "but now",
                            "i am behend wu zhi shan"};
    SpoutOutputCollector collector=null;
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector=spoutOutputCollector;
    }
    private int index = 0;
    public void nextTuple() {
//            String str = data[index];
//            collector.emit(new Values(str),index);
//            index = index == data.length - 1 ? 0 : index+1;
        if(index<data.length){
            String str = data[index];
            collector.emit(new Values(str),index);
            index++;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object msgId) {
        System.out.println(msgId+"发送成功");
        collector.emit(new Values(data[(int) msgId]),msgId);
    }

    @Override
    public void fail(Object msgId) {
        super.fail(msgId+"发送失败,尝试重发");
    }
}
