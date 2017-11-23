package gao.combinefile.test;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;

/**
 * Created by gao on 2016/12/14.
 */
public class CombineSmallFileRecordReader extends RecordReader<LongWritable,BytesWritable> {
    private CombineFileSplit combineFileSplit;
    private LineRecordReader lineRecordReader=new LineRecordReader();
    private Path[] paths;
    private int currentIntex;
    private int tatolLength;
    private LongWritable key;
    private BytesWritable value=new BytesWritable();
    private float inputProgress;

    public CombineSmallFileRecordReader(CombineFileSplit split, TaskAttemptContext context,Integer index) throws IOException {
        super();
        this.combineFileSplit=split;
        this.currentIntex=index;
    }

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        combineFileSplit= (CombineFileSplit) split;
        FileSplit fileSplit=new FileSplit(combineFileSplit.getPath(currentIntex),
                combineFileSplit.getOffset(currentIntex),
                combineFileSplit.getLength(),combineFileSplit.getLocations());
        lineRecordReader.initialize(fileSplit,context);
        paths=combineFileSplit.getPaths();
        tatolLength=paths.length;
        context.getConfiguration().set("input.file.name",combineFileSplit.getPath(currentIntex).getName());
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(currentIntex>0&&currentIntex<tatolLength){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        key=lineRecordReader.getCurrentKey();
        return key;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        byte[] bytes=lineRecordReader.getCurrentValue().getBytes();
        value.set(bytes,0,bytes.length);
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if(currentIntex>0&&currentIntex<tatolLength){
            inputProgress=(float) currentIntex/tatolLength;
        }
        return inputProgress;
    }

    @Override
    public void close() throws IOException {
        lineRecordReader.close();
    }
}
