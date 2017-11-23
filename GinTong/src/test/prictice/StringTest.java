package test.prictice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {
    public static void main(String[] args) {
        String str="abc fgh   tyui";
        System.out.println(reverse3(str));
//        Pattern pattern=Pattern.compile("\\w+",Pattern.CASE_INSENSITIVE);
//        Matcher matcher=pattern.matcher(str);
//        matcher.find();
//        System.out.println(matcher.group());


    }
    public static String reverse2(String str){
        String[] strs=str.split("\\s");
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            String c=reverse(strs[i]);
            builder.append(c+" ");
        }
        return builder.toString();
    }
    public static String reverse(String str){
        char[] chars=str.toCharArray();
        for (int i = 0; i <chars.length/2 ; i++) {
            char tmp=chars[i];
            chars[i]=chars[chars.length-i-1];
            chars[chars.length-i-1]=tmp;
        }
        return new String(chars);
    }
    public static String reverse3(String str){
        Pattern p=Pattern.compile("\\w+");
        Matcher matcher=p.matcher(str);
        StringBuilder builder=new StringBuilder();
        while(matcher.find()){
            builder.append(reverse(matcher.group())+" ");
        }
        return builder.toString();
    }
}
