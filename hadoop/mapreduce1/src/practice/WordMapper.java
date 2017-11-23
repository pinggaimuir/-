package practice;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author gao
 */
public class WordMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line=value.toString();
        //每行字符串长度
        int len=line.length()-1;
        //将字符串变成字符串数组
        String[] strs=new String[len];
        for(int i=0;i<line.length()-1;i++){
            strs[i]=line.charAt(i)+"";
        }
        //遍历字符串数组
        for(int i=0;i<strs.length-1;i++) {
            char ch=strs[i].charAt(0);
            //将大写转小写
            if(ch>='A'&&ch<='Z'){
                strs[i]=String.valueOf((char)(ch+32));
            }
            //将标点符号去除
            if(ch== ','||ch== '.'||ch== '!' ||ch== '?'||ch== ':'||ch== ';'){
                strs[i]="";
            }
            //将数字变成==NUMBER==
            if(ch>=48&&ch<=57){
                strs[i]="==NUMBER==";
                len=len+9;
            }
            //将多个空格变成一个空格
            if(ch==32){
                char ch2=strs[i+1].charAt(0);
                if(ch2==32){
                    strs[i]="";
                    len--;
                }
            }
        }
        //把字符串数组变成字符
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < strs.length; i++){
            sb. append(strs[i]);
        }
        String str=sb.toString();
	    context.write(new Text(str),NullWritable.get());

	}
	
}
