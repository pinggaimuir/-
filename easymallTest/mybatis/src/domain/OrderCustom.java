package domain;

import java.io.Serializable;

/**
 * Orders的扩展类
 * 通过此类映射订单和用户的查询结果，让此类继承字段较多的类
 * Created by tarena on 2016/9/29.
 */
public class OrderCustom extends Orders implements Serializable {
    private String username;
    private String sex;
    private String address;

    @Override
    public String toString() {
        return "OrderCustom{" +
                "address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
