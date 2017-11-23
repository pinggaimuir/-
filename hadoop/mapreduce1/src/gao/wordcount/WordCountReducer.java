//package gao.wordcount;
///**
// * Reducer组件会就收mapper组件的输出结果《mapper.context.write》
// * 第一个形参类型：mapper输出的key类型
// * 第二个形参类型：mapper输出的value类型
// *
// * 1、Mapper组件可以单独工作，但是Reducer工作必须依赖Mapper组件，因为需要Mapper组件的输出结果
// * 2、引入Reducer组件后，输出结果就是Reducer的输出结果
// *
// * reducer工作机制：
// * 1、根据key，把key对应的value值结合在一起，形成Iterable交给程序员
// * 	所以，reduce方法执行的次数取决于Mapper输出的key
// * 2、Reducer会对key进行排序
// */
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
//
//import java.io.IOException;
//
//public class WordCountReducer extends Reducer<Text, IntWritable, Text, Text> {
//
//	@Override
//	protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, Text>.Context context)
//			throws IOException, InterruptedException {
//		MultipleOutputs<Text,Text> outputs=new MultipleOutputs<>(context);
//		String result="";
//		int count=0;
//		for(IntWritable value:values){
//			count++;
//		}
//		result=count+"";
//		outputs.write(key, new Text(result),key.toString());
//	}
//
//
//}
