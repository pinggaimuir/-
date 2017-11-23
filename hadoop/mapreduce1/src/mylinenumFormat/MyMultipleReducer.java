package mylinenumFormat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyMultipleReducer extends Reducer<IntWritable,Text,IntWritable,Text> {
    private MultipleOutputs outs;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        outs=new MultipleOutputs(context);
    }

    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for(Text value:values){
            String val=value.toString();
            if((key.toString().equals("1"))){
                outs.write("world",key,value);
            }else if((key.toString().equals("2"))){
                outs.write("hadoop",key,value);
            }else{
                outs.write("1606",key,value);
            }
        }
    }
}
