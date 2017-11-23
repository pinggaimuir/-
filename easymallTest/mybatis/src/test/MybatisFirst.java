package test;

import domain.User;
import junit.framework.TestCase;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tarena on 2016/9/22.
 */
public class MybatisFirst extends TestCase {
    //得到sqlMap流
    SqlSession sqlSession=null;
    @Override
    protected void tearDown() throws Exception {
        sqlSession.close();
    }
    protected void setUp() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建会话工厂，传入mybitis配置信息
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //通过工厂得到sqlsession
        sqlSession = sqlSessionFactory.openSession();
    }
    /*通过用户id查询用户信息*/
    public void testFindUserById() throws IOException {
        //通过session操纵数据库
        User user=sqlSession.selectOne("test.findUserById","1");
        System.out.println(user.toString());
    }
    /*根据用户的名称进行模糊查询*/
    public void testFindUserByName(){
        List<User> list=  sqlSession.selectList("test.findUserByName","瓶盖");
        sqlSession.commit();
        for(User user:list){
            System.out.println(user.toString());
        }
    }
    //像数据库中插入记录bing返回插入后记录的自增主键
    public void testAddUser(){
        User user1=new User();
        user1.setId("5");
        user1.setPassword("1");
        user1.setUsername("ning");
        user1.setNickname("6177");
        user1.setEmail("123@qq.com");
        user1.setRole("user");
        sqlSession.insert("test.addUser",user1);
        sqlSession.commit();
        System.out.println(user1.getId());
    }

    /*像数据库总插入数据并且返回非自增主键，  用UUID生成的id获得*/
    public void testAddUser2(){
        User user2=new User();
        user2.setPassword("1");
        user2.setUsername("峰峰");
        user2.setNickname("7370");
        user2.setEmail("123@qq.com");
        user2.setRole("user");
        int n=sqlSession.insert("test.addUser2",user2);
        sqlSession.commit();
        System.out.println(n);
    }

    /*根据username删除用户*/
    public void testDelUser(){
        sqlSession.delete("test.delUser","6143");
    }
    /*根据nick更新用户*/
    public void testUptateUser(){
        User user=new User();
        user.setUsername("gao");
        user.setNickname("6143");
        user.setPassword("1");
        sqlSession.update("test.updateUser",user);
    }
}
