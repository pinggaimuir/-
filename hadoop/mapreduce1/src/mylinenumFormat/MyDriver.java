package mylinenumFormat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job= Job.getInstance(conf);

        job.setJarByClass(MyDriver.class);
        job.setMapperClass(MyWordMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(MyMultipleReducer.class);
//        job.setOutputKeyClass(IntWritable.class);
//        job.setOutputValueClass(Text.class);
//        job.setNumReduceTasks(0);
        job.setInputFormatClass(MyInputFormat.class);
        job.setOutputFormatClass(MyOutputFormat.class);

        MultipleOutputs.addNamedOutput(job,"world", TextOutputFormat.class,IntWritable.class,Text.class);
        MultipleOutputs.addNamedOutput(job,"hadoop", TextOutputFormat.class,IntWritable.class,Text.class);
        MultipleOutputs.addNamedOutput(job,"1606", TextOutputFormat.class,IntWritable.class,Text.class);



        FileInputFormat.setInputPaths(job,new Path(args[0]+"/words.txt"));
        FileOutputFormat.setOutputPath(job,new Path("hdfs://192.168.8.105:9000/wordcount"+"/words2"));

        job.waitForCompletion(true);
    }
}
