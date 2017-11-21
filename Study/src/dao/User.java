package dao;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * Created by gao on 2016/9/3.
 */
public class User implements HttpSessionBindingListener {
    public User(){}
    private String username;
    private String password;
    private String nickname;

    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}
