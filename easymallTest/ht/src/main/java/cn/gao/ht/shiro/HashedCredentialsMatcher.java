package cn.gao.ht.shiro;

import cn.gao.ht.tool.MdrHash;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * Created by tarena on 2016/10/13.
 */

public class HashedCredentialsMatcher extends SimpleCredentialsMatcher {
    public HashedCredentialsMatcher(){}


    //    private Cache<String,AtomicInteger> passwordRetryCache;
//    public HashedCredentialsMatcher(CacheManager cacheManager){
//        passwordRetryCache=cacheManager.getCache("passwordRetryCache");
//    }
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
//        String username= (String) token.getPrincipal();
//        AtomicInteger retryCount=passwordRetryCache.get(username);
//        if(retryCount==null){
//            retryCount=new AtomicInteger(0);
//            passwordRetryCache.put(username,retryCount);
//        }
//        //自定义呀拿证过程，用户连续输入密码错误5次以上禁止用户登陆
//        if(retryCount.incrementAndGet()>5){
//            throw new ExcessiveAttemptsException();
//        }
//        boolean match=super.doCredentialsMatch(token,info);
//        if(match){
//            passwordRetryCache.remove(username);
//        }
        //得到当前用户名密码令牌
        UsernamePasswordToken loginToken= (UsernamePasswordToken) token;
        String username=loginToken.getUsername();
        String password=String.valueOf(loginToken.getPassword());
        //md5加密
        password= MdrHash.getMd5Hash(password,username);
        System.out.println(password);
        //将密文密码设置进token
        loginToken.setPassword(password.toCharArray());
        boolean match=super.doCredentialsMatch(loginToken,info);

        return match;
    }
}
