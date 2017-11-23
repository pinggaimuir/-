package mylinenumFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by gao on 2016/12/13.
 */
public class MyOutputFormat<K,V> extends FileOutputFormat<K,V> {
    @Override
    public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Path path=super.getDefaultWorkFile(job,"");
        Configuration conf=new Configuration();
        FileSystem fs=path.getFileSystem(conf);
        FSDataOutputStream out=fs.create(path);
        return new MyRecordWriter<K,V>(out,"|","\r\n");
    }
}
