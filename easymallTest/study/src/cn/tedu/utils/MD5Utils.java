package cn.tedu.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密算法
 * Created by tarena on 2016/9/5.
 */
public class MD5Utils {
    public MD5Utils(){}
    public static String md5(String plainText){
        if(plainText==null)return null;
        byte[] secretBytes=null;
        try {
            secretBytes= MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String md5=new BigInteger(1,secretBytes).toString();
        for(int i=0;i<32-md5.length();i++){
            md5="0"+md5;
        }
        return md5;
    }
}
