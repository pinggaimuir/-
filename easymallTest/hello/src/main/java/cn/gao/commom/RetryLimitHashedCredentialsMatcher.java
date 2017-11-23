package cn.gao.commom;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tarena on 2016/10/13.
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private Cache<String,AtomicInteger> passwordRetryCache;
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager){
        passwordRetryCache=cacheManager.getCache("passwordRetryCache");
    }
    public boolean doCredentialsMach(AuthenticationToken token, AuthenticationInfo info){
        String username= (String) token.getPrincipal();
        AtomicInteger retryCount=passwordRetryCache.get(username);
        if(retryCount==null){
            retryCount=new AtomicInteger(0);
            passwordRetryCache.put(username,retryCount);
        }
        //自定义呀拿证过程，用户连续输入密码错误5次以上禁止用户登陆
        if(retryCount.incrementAndGet()>5){
            throw new ExcessiveAttemptsException();
        }
        boolean match=super.doCredentialsMatch(token,info);
        if(match){
            passwordRetryCache.remove(username);
        }
        //得到当前用户名密码令牌
        UsernamePasswordToken loginToken= (UsernamePasswordToken) token;
        String password=String.valueOf(loginToken.getPassword());
        //md5加密
        password=MdrHash.getMd5Hash(password,username);
        //将密文密码设置进token
        loginToken.setPassword(password.toCharArray());
        return match;
    }
}
