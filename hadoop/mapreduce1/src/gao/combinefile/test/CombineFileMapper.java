package gao.combinefile.test;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by gao on 2016/12/14.
 */
public class CombineFileMapper extends Mapper<LongWritable,Text,Text,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String fileName=context.getConfiguration().get("input.file.name");
        context.write(new Text(fileName),value);
    }
}
