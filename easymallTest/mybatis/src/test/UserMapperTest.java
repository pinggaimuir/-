package test;

import domain.User;
import domain.User22;
import domain.UserCustom;
import domain.UserQueryVo;
import junit.framework.TestCase;
import mappertest9_23.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper代理方式
 * sqlSession是线程不安全的 最好写在方法中，方法执行的时候进虚拟机栈，属于线程私有
 * Created by tarena on 2016/9/23.
 */
public class UserMapperTest extends TestCase {
    private SqlSessionFactory sqlSessionFactory=null;
    public void setUp() throws IOException {
        InputStream input= Resources.getResourceAsStream("SQLMapConfig.xml");
        sqlSessionFactory=new  SqlSessionFactoryBuilder().build(input);
    }
    public void testFindUserByNickname(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        /*userMapper为代理对象*/
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        /*如果返回类型为List,则代理对象调用的sqlSession的selectList,若为单个记录 则为selectOne*/
//        List<User> user= userMapper.findUserByNickname("6143");
//        System.out.println(user.toString());
    }
    public void testAddUser(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        User user=new User();
        user.setPassword("1");
        user.setUsername("正峰");
        user.setNickname("7370");
        user.setEmail("123@qq.com");
        user.setRole("user");
        userMapper.addUser(user);
    }
    //查询用户列表
    public void testFindListUserCustom(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        UserCustom user=new UserCustom();
        user.setPassword("1");
//        user.setUsername("正峰");
//        user.setNickname("7370");
        user.setEmail("123@qq.com");
//        user.setRole("user");
        UserQueryVo qvo=new UserQueryVo();
        qvo.setUserCustom(user);
        List<UserCustom> userlist=userMapper.findUserList(qvo);
        System.out.println(userlist.toString());

    }
    //查询用户列表用restltMap
    public void testfindUserResult(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        UserCustom user=new UserCustom();
        user.setPassword("1");
        user.setUsername("正峰");
        user.setNickname("7370");
        user.setEmail("123@qq.com");
        user.setRole("user");
        UserQueryVo qvo=new UserQueryVo();
        qvo.setUserCustom(user);
        List<UserCustom> userlist=userMapper.findUserResult(qvo);
        System.out.println(userlist.toString());

    }
    public void testfindUserByMultiId(){
        SqlSession sqlSession=sqlSessionFactory.openSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        UserCustom user=new UserCustom();
        user.setPassword("1");
//        user.setUsername("正峰");
//        user.setNickname("7370");
        user.setEmail("123@qq.com");
        List<Integer> list=new ArrayList();
        list.add(1);
        list.add(3);
        list.add(5);
        user.setRole("user");
        UserQueryVo qvo=new UserQueryVo();
        qvo.setUserCustom(user);
        qvo.setIds(list);
        List<UserCustom> userlist=userMapper.findUserByMultiId(qvo);
        System.out.println(userlist.toString());
    }
    public void testSelectUserOrderByAge() throws Exception{
        SqlSession sqlsession=sqlSessionFactory.openSession();
//        UserMapper mapper=sqlsession.getMapper(UserMapper.class);
		/*Map<String,Object> map=new HashMap();
		map.put("id",3);
		map.put("id2",5);*/

        List<User22> users=sqlsession.selectList("selectUserOrderByAge","age");
        for(User22 user:users){
            System.out.println(user.toString());
        }
        sqlsession.commit();
        sqlsession.close();
    }
}
