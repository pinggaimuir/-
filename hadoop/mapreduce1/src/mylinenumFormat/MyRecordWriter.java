package mylinenumFormat;


import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyRecordWriter<K,V> extends RecordWriter<K,V> {
    private FSDataOutputStream out;
    private String keyValueSeparater;
    private String lineSeparater;

    public MyRecordWriter(FSDataOutputStream out, String s, String s1) {
        this.out=out;
        this.keyValueSeparater=s;
        this.lineSeparater=s1;
    }
    //key 就是输出key。如果job任务里没有reduce，key就是mapper输出key
    //如果有reduce，key时reduce的输出key
    //输出value
    //注意：写的时候注意顺序：key 分割符 value 行分割符
    //这个是决定结果里的格式的，针对mapper输出的结果文件同样生效
    @Override
    public void write(K key, V value) throws IOException, InterruptedException {
        out.write(key.toString().getBytes());
        out.write(keyValueSeparater.getBytes());
        out.write(value.toString().getBytes());
        out.write(lineSeparater.getBytes());
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        out.close();
    }
}
