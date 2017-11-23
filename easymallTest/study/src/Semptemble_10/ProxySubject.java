package Semptemble_10;

import java.lang.reflect.Proxy;

/**
 * Created by tarena on 2016/9/10.
 */
public class ProxySubject implements Subject1 {
    private Subject1  delegate;
    public ProxySubject(Subject1 delegate){
        this.delegate=delegate;
    }
    public void token(){
        System.out.println();
    }
    Proxy p=null;
}
