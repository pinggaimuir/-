package maxtemp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by gao on 2016/12/10.
 */
public class MaxTemperatureDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf=new Configuration();
        //设置map任务输出gzip压缩格式的代码
//        conf.setBoolean("mapred.compress.map.ouput",true);
//        conf.setClass("mapred.map.output.compress.codec",GzipCodec.class,CompressionCodec.class);

        Job job=Job.getInstance(conf);
        job.setJarByClass(MaxTemperatureDriver.class);
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setCombinerClass(MaxTemperatureReducer.class);



        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]+"/maxtemp/zip"));
        FileOutputFormat.setOutputPath(job,new Path(args[1]+"/maxtemp/"));
        //reduce压缩输出的api
//        FileOutputFormat.setCompressOutput(job,true);
//        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);



        System.exit(job.waitForCompletion(true)?0:1);
    }
}
