package cn.gao.storm2;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ReportBolt extends BaseRichBolt {
	
	private Map<String,Integer> map = new HashMap<>();
	private OutputCollector collector = null;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		try {
			String word = input.getStringByField("word");
			int count = input.getIntegerByField("count");
			map.put(word, count);
			
			System.out.println("---------------ע单词数量发生变化"+word+"~"+count+"---------");
			
			collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	@Override
	public void cleanup() {
		System.out.println("##############################################");
		for(Map.Entry<String, Integer>entry:map.entrySet()){
			String str = entry.getKey();
			int count = entry.getValue();
			System.out.println(str + ":" + count);
		}
		System.out.println("##############################################");
	}
	
}
