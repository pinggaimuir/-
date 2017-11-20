package cn.tedu.service;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;

import java.util.List;

/**
 * Created by tarena on 2016/9/9.
 */
public interface OrderService {
    void addOrder(Order order, List<OrderItem> list);
}
