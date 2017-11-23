package cn.gao.SequeceFile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;

import java.net.URI;

/**
 * Created by gao on 2016/12/11.
 */
public class MapFileFixer {
    public static void main(String[] args) throws Exception {
        String uri="mapsequence.text";
        Configuration conf=new Configuration();
        FileSystem fs=FileSystem.get(URI.create(uri), conf);
        Path map=new Path(uri);
        Path mapData=new Path(map, MapFile.DATA_FILE_NAME);
        SequenceFile.Reader reader=new SequenceFile.Reader(fs,mapData,conf);
        Class keyClass=reader.getKeyClass();
        Class valueVlass=reader.getValueClass();
        reader.close();
        //�ؽ�����
        long entries=MapFile.fix(fs,map,keyClass,valueVlass,false,conf);
        System.out.printf("Created Mapfie %s with %d entries\n",map,entries);
    }
}
