package gao.combinefile;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class CombineSmallFileMapper extends Mapper<LongWritable,ByteWritable,Text,Text>{
    private Text file=new Text();
    @Override
    protected void map(LongWritable key, ByteWritable value, Context context) throws IOException, InterruptedException {
        String filename=context.getConfiguration().get("map.input.file.name");
        file.set(filename);

        context.write(file,new Text(new String(value.toString().trim())));
    }
}
