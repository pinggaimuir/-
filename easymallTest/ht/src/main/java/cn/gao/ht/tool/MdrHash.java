package cn.gao.ht.tool;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Created by tarena on 2016/10/20.
 */
public class MdrHash {
    public static String getMd5Hash(String password,String username){
        /**
         * md5哈希算法
         * 第一个参数为要加密的密码
         * 第二个参数为盐
         * 第三个参数为加密次数
         */
        Md5Hash md5Hash=new Md5Hash(password,username,3);
        return md5Hash.toString();
    }
}
