package cn.tedu.dao;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.utils.DaoUtils;

import java.sql.Timestamp;

/**
 * Created by tarena on 2016/9/9.
 */

public class OrderDaoImpl implements OrderDao {
    public void addOrder(Order order) {
        String sql="insert into orders (id,money,receiverinfo,paystate,ordertime,user_id) values(?,?,?,?,?,?)";
        Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
        Object[] params={order.getId(),order.getMoney(),order.getReceiverinfo(),order.getPaystate(),timestamp,order.getUser_id()};
        DaoUtils.txupdate(sql,params);
    }

    public void addOrderItem(OrderItem item) {
        String sql="insert into orderitem (order_id,product_id,buynum) values(?,?,?)";
        Object[] params={item.getOrder_id(),item.getProdect_id(),item.getBuynum()};
        DaoUtils.txupdate(sql,params);

    }

    @Override
    public int editPnumtx(String id, int newNum) {
        String sql="update products set pnum=? where id=?";
        Object[] params={newNum,id};
       return  DaoUtils.txupdate(sql,params);
    }
}
