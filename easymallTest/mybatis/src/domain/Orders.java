package domain;

import java.io.Serializable;
import java.util.*;

/**
 * Created by tarena on 2016/9/29.
 */
public class Orders implements Serializable {
    private User2 user;//用户信息

    private int id;
    private int user_id;
    private String number;
    private Date createtime;
    private String note;
    private List<Orderdetail> orderDetails;//订单明细
    public List<Orderdetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Orderdetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public User2 getUser() {
        return user;
    }

    public void setUser(User2 user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "createtime=" + createtime +
                ", user=" + user +
                ", orderDetails=" + orderDetails +
                ", id=" + id +
                ", user_id=" + user_id +
                ", number='" + number + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
