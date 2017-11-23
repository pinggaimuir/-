package cn.gao.pojo;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by gao on 2017/3/5.
 */
public class AppTest {
    Session session=null;
    @Before
    public void init(){
        //创建配置对象
        Configuration configuration=new Configuration().configure("hibernate.cfg.xml");
        //创建服务注册对象
        SessionFactory factory=configuration.buildSessionFactory();
        session=factory.openSession();
    }

    @Test
    public void testSave() throws Exception{
        User5 user=new User5();
        user.setName("lisi");
        //保存
        Transaction tx=session.beginTransaction();
        session.save(user);
        tx.commit();
        session.close();
    }
    @Test
    public void testGet() throws Exception{
        User5 user=(User5)session.get(User5.class,1);
        System.out.println(user);
        session.close();
    }
}
