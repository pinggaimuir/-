package cn.tedu.dao;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;

/**
 * Created by tarena on 2016/9/9.
 */
public interface OrderDao {
    void addOrder(Order order);
    void addOrderItem(OrderItem item);
    int editPnumtx(String id,int newNum);

}
