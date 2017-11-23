package cn.gao.storm2;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordCountBolt extends BaseRichBolt{

	private Map<String,Integer> map = new HashMap<>();
	private OutputCollector collector = null;
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		try {
			String word = input.getStringByField("word");
			map.put(word, map.containsKey(word) ? map.get(word) + 1 : 1);
			collector.emit(input,new Values(word,map.get(word)));
			
			collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word","count"));
	}

}
