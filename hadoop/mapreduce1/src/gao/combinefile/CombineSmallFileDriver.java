//package gao.combinefile;
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
// * Created by gao on 2016/12/13.
// */
//public class CombineSmallFileDriver {
//    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//        Configuration conf=new Configuration();
//        Job job= Job.getInstance(conf);
//
//        job.setJarByClass(CombineSmallFileDriver.class);
//        job.setMapperClass(CombineSmallFileMapper.class);
//
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(Text.class);
////        job.setInputFormatClass(CombineSmallFileInputFormat.class);
//
//        FileInputFormat.setInputPaths(job,new Path(args[0]+"/combine"));
//        FileOutputFormat.setOutputPath(job,new Path("hdfs:192.168.8.105:9000/wordcount/combineFile"));
//
//        System.exit(job.waitForCompletion(true)?0:1);
//    }
//}
