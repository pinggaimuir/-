package gao.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * MR计算有两个组件，分别是Mapper和reducer
 * Mapper组件
 * @author gao
 *
 */
public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		String line=value.toString();
		String[] data=line.split(" ");
		for(String word:data){
			context.write(new Text(word),new IntWritable(1));
		}
	}
	
}
