package cn.gao.storm2;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseRichBolt {

	private OutputCollector collector = null;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	private int count = 0;
	@Override
	public void execute(Tuple input) {
		try {
			String sentence = input.getStringByField("sentence");
			
			if("are you sure you do not like me".equals(sentence) && count < 3){
				count ++;
				throw new RuntimeException("人为发送不出去~~~~");
			}
			
			String [] words = sentence.split(" ");
			for(String word : words){
				collector.emit(input,new Values(word));
			}
			
			collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
