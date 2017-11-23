package practice;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordDriver {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf);
		
		//指定Job工作运行的主类
		job.setJarByClass(WordDriver.class);
		//指定Mapper组件的运行类
		job.setMapperClass(WordMapper.class);
		//指定reducer的运行类
//		job.setReducerClass(WordReducer.class);
		
//		指定mapper组件输出key的类型
		job.setMapOutputKeyClass(Text.class);
//		指定maooer组件输出的value类型，设置Text
		job.setMapOutputValueClass(NullWritable.class);
		
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(NullWritable.class);

		job.setNumReduceTasks(0);
		
		//设置job处理文件所在的HDFS
//		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.8.105:9000/wordcount/words.txt"));
		//mr处理生成的结果是以文件的形式存储的，这个结果文件必须放在HDFS上，这个输出路径需要指定，会自行创建
//		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.8.105:9000/wordcount/result2"));
		FileInputFormat.setInputPaths(job,new Path(args[0]+"/test_input.txt"));
		FileOutputFormat.setOutputPath(job,new Path(args[1]+"/test_output.txt"));
		job.waitForCompletion(true);
	}
}
