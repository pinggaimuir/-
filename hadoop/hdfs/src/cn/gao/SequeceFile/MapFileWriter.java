package cn.gao.SequeceFile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

import java.io.IOException;
import java.net.URI;

/**
 * Created by gao on 2016/12/11.
 */
public class MapFileWriter {
    private static final String[] DATA={
            "One,two,buckle my shoe",
            "Three,four,shut the door",
            "Five,six,pick up sticks",
            "Seven,eight,lay them straight",
            "Nine,ten,a big fat hen"
    };

    public static void main(String[] args) throws IOException {
        String uri="mapsequence.text";
        Configuration conf=new Configuration();
        FileSystem fs=FileSystem.get(URI.create(uri),
                conf);
        Path path=new Path(uri);

        IntWritable key=new IntWritable();
        Text value=new Text();
        MapFile.Writer writer=null;
        try{
            writer=new MapFile.Writer(conf,fs,uri,key.getClass(),value.getClass());
            for(int i=0;i<1024  ;i++){
                key.set(i+1);
                value.set(DATA[i%DATA.length]);
                System.out.printf("%s\t%s\n",key,value);
                writer.append(key,value);
            }
        }finally {
            IOUtils.closeStream(writer);
        }

    }
}
