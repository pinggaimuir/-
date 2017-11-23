package domain;

import java.io.Serializable;

/**
 * Created by tarena on 2016/9/29.
 */
public class Orderdetail implements Serializable {
    private int id;
    private int orders_id;
    private int items_id;
    private int items_num;
    private Items items;

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Orderdetail{" +
                "id=" + id +
                ", orders_id=" + orders_id +
                ", items_id=" + items_id +
                ", items_num=" + items_num +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItems_id() {
        return items_id;
    }

    public void setItems_id(int items_id) {
        this.items_id = items_id;
    }

    public int getItems_num() {
        return items_num;
    }

    public void setItems_num(int items_num) {
        this.items_num = items_num;
    }

    public int getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(int orders_id) {
        this.orders_id = orders_id;
    }
}
