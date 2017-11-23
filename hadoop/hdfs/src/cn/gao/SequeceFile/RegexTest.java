package cn.gao.SequeceFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gao on 2017/1/8.
 */
public class RegexTest {
    public static void main(String[] args) {
////        String rex="^\\w+?@(\\w+?)(?:\\.\\w+?)+$";
////        String rex="[^\\|]*?(\\|\\w+?)+\\d+?_0_(\\d+?)\\|\\w+";
//        String rex="_0_(.*?)\\|";
//        String str="http://nfdnfwww@sina.com.cnkgjlhagd|dfsh|4325432_0_4354543653|dhfsgfgfdsgfgd|fg";
//        Pattern p=Pattern.compile(rex);
//        Matcher m=p.matcher(str);
//        m.find();
//        System.out.printf(m.group(1));





        String sql="select count * fom flux where reportTime='2017-01-08'";
        //
        String sql2="select count(distinct statuv) from flux where reportTime='2017-01-08'";
    }
}
