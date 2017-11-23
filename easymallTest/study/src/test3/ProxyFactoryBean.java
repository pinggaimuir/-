package test3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by tarena on 2016/9/13.
 */
public class ProxyFactoryBean {
    private Advice advice;
    private Object target;

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        Object proxy2 = Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] params)throws Throwable {
                            advice.beforeMethod();
                        Object result=method.invoke(target,params);
                        return result;
                    }
                }
        );
        return proxy2;
    }
}



