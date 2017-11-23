package cn.gao.storm2;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SententSpout extends BaseRichSpout {

	private static String [] strs = {
		"i am so shuai",
		"do you like me",
		"are you sure you do not like me",
		"ok i am sure"
	};
	
	private SpoutOutputCollector collector = null;
	
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collecr) {
		this.collector = collecr;
	}

	private int index = 0;
	@Override
	public void nextTuple() {
//		String str = strs[index];
//		collector.emit(new Values(str));
//		index = index == strs.length-1 ? 0 : index+1;
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		if(index<strs.length){
			String str = strs[index];
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
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}
	
	@Override
	public void fail(Object msgId) {
		System.out.println("##########发送失败"+msgId+":"+strs[(int) msgId] + "######正在尝试重发####");
		collector.emit(new Values(strs[(int) msgId]),msgId);
	}

	@Override
	public void ack(Object msgId) {
		System.out.println("##########发送成功"+msgId+":"+strs[(int) msgId] + "##########");
	}
}
