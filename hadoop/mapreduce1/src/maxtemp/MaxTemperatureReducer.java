package maxtemp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by gao on 2016/12/10.
 */
public class MaxTemperatureReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int maxValue=Integer.MIN_VALUE;
//        MultipleOutputs<Text,IntWritable> outputs=new MultipleOutputs<>(context);
        for(IntWritable value:values){
            maxValue=Math.max(maxValue,value.get());
        }
        context.write(key,new IntWritable(maxValue));
    }
}
