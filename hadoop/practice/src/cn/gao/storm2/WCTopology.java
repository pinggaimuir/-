package cn.gao.storm2;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class WCTopology {
	private static final String SENTENCE_SPOUT_ID = "sentence-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String REPORT_BOLT_ID = "report-bolt";
	private static final String TOPOLOGY_NAME = "word-count-topology";
	
	public static void main(String[] args) throws Exception {
		//--��������
		SententSpout spout = new SententSpout();
		SplitSentenceBolt splitBolt = new SplitSentenceBolt();
		WordCountBolt wcBolt = new WordCountBolt();
		ReportBolt reportBolt = new ReportBolt();
		
		//--����topology������
		TopologyBuilder builder = new TopologyBuilder();
		
		//--��֪������topology�Ľṹ
		builder.setSpout(SENTENCE_SPOUT_ID, spout);
		builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(SENTENCE_SPOUT_ID);
		builder.setBolt(COUNT_BOLT_ID, wcBolt).fieldsGrouping(SPLIT_BOLT_ID,new Fields("word"));
		builder.setBolt(REPORT_BOLT_ID, reportBolt).globalGrouping(COUNT_BOLT_ID);
		
		//--ͨ�������ߴ���topology
		StormTopology topology = builder.createTopology();
		
		//--�ϴ�topology����Ⱥ������ -- ��Ⱥ����ʱ��д��
		//Config conf = new Config();
		//StormSubmitter.submitTopology(TOPOLOGY_NAME, conf, topology);
		
		//--ִ��topology -- ��������ʱ��д��
		//----����LocalCluster�����ڱ���ģ��һ����Ⱥ
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(TOPOLOGY_NAME, conf, topology);
		
		
		//--�ü�Ⱥ����10����Զ��˳�
		Thread.sleep(1000 * 10);
		cluster.killTopology(TOPOLOGY_NAME);
		cluster.shutdown();
	}
}
