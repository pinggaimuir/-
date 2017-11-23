package mappertest9_23;

import domain.OrderCustom;
import domain.Orders;
import domain.User2;

import java.util.List;

/**
 * 订单的mapper
 * Created by tarena on 2016/9/29.
 */
public interface OrdersMapperCustom {
    /*订单个用户信息关联查询*/
    List<OrderCustom> findUserOrder()throws Exception;
    List<Orders> findOrdersUserResultMap()throws Exception;
    /*查询订单 关联用户信息 订单明细信息*/
    List<Orders> findOrdersAndOrderDetailResultMap()throws Exception;
    User2 findUsersEtc()throws Exception;
    List<Orders> findOrdersUserLazyLoading()throws Exception;
}
