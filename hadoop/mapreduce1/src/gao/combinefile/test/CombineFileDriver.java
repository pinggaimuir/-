//package gao.combinefile.test;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//
//import java.io.IOException;
//
///**
// * Created by gao on 2016/12/14.
// */
//public class CombineFileDriver {
//    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//        Configuration conf=new Configuration();
//        Job job=Job.getInstance(conf);
//
//        job.setInputFormatClass(CombineSmallFileInput.class);
//        job.setMapperClass(CombineFileMapper.class);
//
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(Text.class);
//
//        FileInputFormat.setInputPaths(job,args[0]+"/combine2");
//        FileOutputFormat.setOutputPath(job,new Path("hdfs://192.168.8.105:9000/wordcount/combine2"));
//
//        job.waitForCompletion(true);
//    }
//}
