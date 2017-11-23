package com.gao.springjdbc;

import com.gao.springjdbc.domain.User3;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存Cache
 * Created by tarena on 2016/9/24.
 */
@Aspect
@Component
public class AspectCache4 {
    static Map<Integer,User3> cacheMap=new HashMap<Integer,User3>();
    @Around("execution(* com.gao.springjdbc.dao.*.find*(int))&&args(id)")
    public Object cacheAround(ProceedingJoinPoint joinPoint, int id) throws Throwable {
        User3 user=cacheMap.get(id);
        if(user==null){
            user= (User3) joinPoint.proceed();
            cacheMap.put(user.getId(),user);
        }
            return user;
    }
}
