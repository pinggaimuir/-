package test;

import domain.User2;
import junit.framework.TestCase;
import mappertest9_23.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by tarena on 2016/10/5.
 */
public class CacheTest extends TestCase {
    private SqlSessionFactory sqlSessionFactory=null;

    protected void setUp() throws Exception {
        InputStream input= Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory=new  SqlSessionFactoryBuilder().build(input);
    }
    /*测试一级缓存*/
    public void testCache1(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        //第一次发起请求，查询id为1的用户
        System.out.println(userMapper.findUserById(1));
        //中间执行一次更新操作
        User2 user=new User2();
        user.setId(2);
        user.setUsername("高健");
        user.setAddress("古县");
        user.setSex("男");
        userMapper.updateUser2(user);
        sqlSession.commit();
        //第二次发起请求，查询id为1的用户
        System.out.println(userMapper.findUserById(1));
        sqlSession.close();
    }
    public void testCache2(){
        SqlSession sqlSession1=sqlSessionFactory.openSession();
        SqlSession sqlSession2=sqlSessionFactory.openSession();
        SqlSession sqlSession3=sqlSessionFactory.openSession();
        //第一次发起请求，查询id为1的用户
        UserMapper userMapper1=sqlSession1.getMapper(UserMapper.class);
        System.out.println(userMapper1.findUserById(1));
        sqlSession1.close();
        //中间执行一次更新操作
 /*       UserMapper userMapper3=sqlSession3.getMapper(UserMapper.class);
        User2 user=new User2();
        user.setId(1);
        user.setUsername("李石锐");
        user.setAddress("古县");
        user.setSex("男");
        userMapper3.updateUser2(user);
        sqlSession3.commit();
        sqlSession3.close();*/
        //第二次发起请求，查询id为1的用户
        UserMapper userMapper2=sqlSession2.getMapper(UserMapper.class);
        System.out.println(userMapper2.findUserById(1));
        sqlSession2.close();
    }
}
