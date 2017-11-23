//package gao.combinefile;
//
//import org.apache.hadoop.io.BytesWritable;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.mapreduce.InputSplit;
//import org.apache.hadoop.mapreduce.RecordReader;
//import org.apache.hadoop.mapreduce.TaskAttemptContext;
//import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.CombineFileRecordReader;
//import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
//
//import java.io.IOException;
//
///**
// * Created by gao on 2016/12/13.
// */
//public class CombineSmallFileInputFormat extends CombineFileInputFormat<LongWritable,BytesWritable> {
//    @Override
//    public RecordReader<LongWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException {
//        CombineFileSplit combineFileSplit= (CombineFileSplit) split;
//        //һ��Ҫͨ��CombineFileRecordReader������һ��RecordReader
//        //���췽����������������ͺ�˳�򣬹��췽��������������
//        CombineFileRecordReader<LongWritable,BytesWritable> recordReader=
//                new CombineFileRecordReader<>(combineFileSplit,context,CombineSmallFileRecordReader.class);
//        try {
//            recordReader.initialize(combineFileSplit,context);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return recordReader;
//    }
//}
