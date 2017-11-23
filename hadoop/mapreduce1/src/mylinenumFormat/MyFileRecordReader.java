package mylinenumFormat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyFileRecordReader extends RecordReader {
    private FileSplit fileSplit;
    private Configuration conf;
    private IntWritable key;
    private Text value;
    private LineReader reader;
    private int count;
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        this.fileSplit= (FileSplit) split;
        this.conf=context.getConfiguration();
        Path path=fileSplit.getPath();
        FileSystem fs=path.getFileSystem(conf);
        //�õ�������
        FSDataInputStream in=fs.open(path);
        reader=new LineReader(in);
    }

    //���η����ķ���ֵΪtrueʱ���ͻᱻ����
    //ÿ��nextKeyValue���������Ѹ�getCurrentKey��getCurrentValue�ͻᱻ����һ��
    //ÿ�ε���һ�ξͻ��getCurrentKey�ͻ�ѷ���ֵ����map������key
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        key=new IntWritable();
        value=new Text();
        Text tmp=new Text();
        int result=reader.readLine(tmp);
        if(result==0){
            //֤��û�����ݿɶ�
            return false;
        }else{
            count++;
            key.set(count);
            value=tmp;
            return true;
        }
    }

    @Override
    public Object getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public Object getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        if(reader!=null){
            reader=null;
        }
    }
}
