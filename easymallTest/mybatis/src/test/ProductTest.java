package test;

import domain.OrderCustom;
import domain.Orders;
import domain.User2;
import junit.framework.TestCase;
import mappertest9_23.OrdersMapperCustom;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * 订单查询
 * Created by tarena on 2016/9/29.
 */
public class ProductTest extends TestCase {
    private SqlSessionFactory sqlSessionFactory;
    protected void setUp() throws Exception {
        InputStream input = Resources.getResourceAsStream("sqlMapConfig.xml");
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(input);
    }
    /*订单用户内链接查询 一对一 resultType模式*/
    public void testOrderMapperCustom() throws Exception {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        OrdersMapperCustom ordersMapperCustom=sqlSession.getMapper(OrdersMapperCustom.class);
        List<OrderCustom> list= ordersMapperCustom.findUserOrder();
        System.out.println(list);
        sqlSession.close();
    }
        /*订单用户内链接查询 一对一 resultMap模式*/
    public void testFindOrdersUserResultMap() throws Exception {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        OrdersMapperCustom ordersMapperCustom=sqlSession.getMapper(OrdersMapperCustom.class);
        List<Orders> list=ordersMapperCustom.findOrdersUserResultMap();
        System.out.println(list.toString());
        sqlSession.close();
    }
    /*查询订单 关联用户信息 订单明细信息*/
    public void testFindOrdersAndOrderDetailResultMap() throws Exception {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        OrdersMapperCustom ordersMapperCustom=sqlSession.getMapper(OrdersMapperCustom.class);
        List<Orders> list=ordersMapperCustom.findOrdersAndOrderDetailResultMap();
        System.out.println(list.toString());
        sqlSession.close();
    }
    /*查询用户 关联订单信息 订单明细 商品信息*/
    public void testFindUserEtcResultMap() throws Exception {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        OrdersMapperCustom ordersMapperCustom=sqlSession.getMapper(OrdersMapperCustom.class);
        User2 user=ordersMapperCustom.findUsersEtc();
        System.out.println(user.toString());
        sqlSession.close();
    }
    /*查询订单关联查询用户   用户信息使用延迟加载*/
    public void testfindOrdersUserLazyLoading() throws Exception {
        SqlSession sqlSession=sqlSessionFactory.openSession();
        OrdersMapperCustom ordersMapperCustom=sqlSession.getMapper(OrdersMapperCustom.class);
        //查询订单信息
        List<Orders> orders=ordersMapperCustom.findOrdersUserLazyLoading();

        for(Orders order:orders){
            User2 user=order.getUser();
            System.out.println(user);
         }
    }
}
