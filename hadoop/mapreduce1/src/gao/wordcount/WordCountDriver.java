package gao.wordcount;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//
//import java.io.IOException;
//
///**
// * 输出结果key：每行行首偏移量
// * 		vlaue：每行内容
// * Mapper第一个形参对用的是key，所以LongWritable，这个形参是固定的
// * 第二个形参对应的是value，应为value是一行一行的内容，所以是Text，这个参数是固定的
// *
// * 1、Mapper组件会读取文件内容，并通过map方法的两个形参key，value，交给程序员，我们最主要的是拿到value，即每行内容
// * 2、每有一行数据就会触发一次map方法
// * 3、第三个和第四个形参，要和context输出的key和value类型对应
// * @author gao
// *
// */
//public class WordCountDriver {
//	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//		Configuration conf=new Configuration();
//		Job job=Job.getInstance(conf);
//
//		//指定Job工作运行的主类
//		job.setJarByClass(WordCountDriver.class);
//		//指定Mapper组件的运行类
//		job.setMapperClass(WordCountMapper.class);
//		//指定reducer的运行类
//		job.setReducerClass(WordCountReducer.class);
//
//		//指定mapper组件输出key的类型
//		job.setMapOutputKeyClass(Text.class);
//		//指定maooer组件输出的value类型，设置Text
//		job.setMapOutputValueClass(IntWritable.class);
//
//		job.setOutputKeyClass(Text.class);
////		job.setOutputValueClass(Text.class);
//
//		job.setNumReduceTasks(0);
//
//		//设置job处理文件所在的HDFS
////		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.8.105:9000/wordcount/words.txt"));
//		//mr处理生成的结果是以文件的形式存储的，这个结果文件必须放在HDFS上，这个输出路径需要指定，会自行创建
////		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.8.105:9000/wordcount/result2"));
//		FileInputFormat.setInputPaths(job,new Path(args[0]+"/words.txt"));
//		FileOutputFormat.setOutputPath(job,new Path(args[1]+"/wordcount1"));
//		job.waitForCompletion(true);
//	}
//}
