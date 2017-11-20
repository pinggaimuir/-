package cn.tedu.domain;

import java.io.Serializable;

/**
 * Created by tarena on 2016/9/9.
 */
public class OrderItem implements Serializable {
    private String  order_id;
    private String  prodect_id;
    private int buynum;



    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public String getProdect_id() {
        return prodect_id;
    }

    public void setProdect_id(String prodect_id) {
        this.prodect_id = prodect_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
