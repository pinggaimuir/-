package cn.tedu.service;

import cn.tedu.dao.OrderDao;
import cn.tedu.dao.ProdDao;
import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.domain.Product;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;
import cn.tedu.utils.TransactionManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/9.
 */
public class OrderServiceImpl implements OrderService {

    public void addOrder(Order order, List<OrderItem> list) {
        OrderDao orderDao= BasicFactory.getFactory().getInstance(OrderDao.class);
        ProdDao prodDao= BasicFactory.getFactory().getInstance(ProdDao.class);
        try {
            /*开启事物*/
            TransactionManager.startTransaction();
            orderDao.addOrder(order);
            for(OrderItem item:list){
                /*根据id查找数据库中的*/
                Product prod= prodDao.findProdByIdtx(item.getProdect_id());
                if(prod.getPnum()>=item.getBuynum()){
                        orderDao.editPnumtx(prod.getId(),prod.getPnum()-item.getBuynum());
                        orderDao.addOrderItem(item);
                }else{
                    throw  new MsgException("商品库存不足！商品id:"+prod.getId()+",商品名"+prod.getName());
                }
            }
            /*提交事物*/
            TransactionManager.commitTranscation();
        } catch (SQLException e) {
            try {
                TransactionManager.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (MsgException e) {
            try {
                TransactionManager.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
