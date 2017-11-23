package cn.gao.storm.random;

import java.io.*;

/**
 * Created by 高健 on 2017/3/9.
 */
public class Test {
    public static void main(String[] args) {
        try {
            String path=Test.class.getClassLoader().getResource("test_input.txt").getPath();
            BufferedReader input=new BufferedReader(new FileReader(path));
            BufferedWriter writer=new BufferedWriter(new FileWriter("test_output.text"));
            String line=null;
            while((line=input.readLine())!=null){
                //将字符串变成字符串数组
                String[] strs=new String[line.length()];
                for(int i=0;i<line.length();i++){
                    strs[i]=line.charAt(i)+"";
                }
                int len=strs.length;
                //遍历字符串数组
                for(int i=0;i<strs.length;i++) {
                    char ch=strs[i].charAt(0);
                    //将大写转小写
                    if(ch>=65&&ch<=90){
                        strs[i]=String.valueOf((char)(ch+32));
                    }
                    //将标点符号去除
                    if(ch== ','||ch== '.'||ch== '!' ||ch== '?'||ch== ':'||ch== ';'){
                        strs[i]="";
                        len--;
                    }
                    //将数字变成==NUMBER==
                    if(ch>=48&&ch<=57){
                        if(i!=len) {
                            char ch2 = strs[i + 1].charAt(0);
                            if ((ch2 >= 48 && ch2 <= 57)) {
                                strs[i] = "";
                                len--;
                            } else {
                                strs[i] = "==NUMBER==";
                                len = len + 9;
                            }
                        }else {
                            strs[i] = "==NUMBER==";
                            len = len + 9;
                        }
                    }
                    //将两个空格变成一个空格
                    if(ch==32){
                        if(i!=len&&32==strs[i + 1].charAt(0)) {
                                strs[i] = "";
                                len--;
                        }
                    }
                }
                //把字符串数组变成字符
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < strs.length; i++){
                    sb. append(strs[i]);
                }
                if(len==0){
                    sb.append("[REMOVED]");
                }
                sb.append("\n");
                String str=sb.toString();
                writer.write(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
