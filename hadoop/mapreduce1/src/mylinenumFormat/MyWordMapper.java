package mylinenumFormat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyWordMapper extends Mapper<IntWritable,Text,IntWritable,Text> {


    @Override
    protected void map(IntWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(key,value);
    }
}
