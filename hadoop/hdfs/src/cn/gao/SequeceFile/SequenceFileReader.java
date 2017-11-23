package cn.gao.SequeceFile;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;

/**
 * Created by gao on 2016/12/11.
 */
public class SequenceFileReader {
    public static void main(String[] args) throws IOException {
        String uri="sequence.text";
        Configuration conf=new Configuration();
        FileSystem fs=FileSystem.get(URI.create(uri),conf);
        Path path=new Path(uri);

        SequenceFile.Reader reader=null;
        try{
            reader=new SequenceFile.Reader(fs,path,conf);
            Writable key= (Writable) ReflectionUtils.newInstance(reader.getKeyClass(),conf);
            Writable value= (Writable) ReflectionUtils.newInstance(reader.getValueClass(),conf);
            long position=reader.getPosition();
            while(reader.next(key,value)){
                //Ìæ»»ÌØÊâ×Ö·û´®£¬Í¬²½
                String syncSeen=reader.syncSeen()?"*":"";
                System.out.printf("[%s%s]\t%s\t%s\n",position,syncSeen,key,value);
                position=reader.getPosition();
            }
        }finally {
            IOUtils.closeStream(reader);
        }
    }
}
