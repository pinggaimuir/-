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
    //key �������key�����job������û��reduce��key����mapper���key
    //�����reduce��keyʱreduce�����key
    //���value
    //ע�⣺д��ʱ��ע��˳��key �ָ�� value �зָ��
    //����Ǿ��������ĸ�ʽ�ģ����mapper����Ľ���ļ�ͬ����Ч
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
