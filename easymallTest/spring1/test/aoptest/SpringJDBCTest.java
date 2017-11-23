package aoptest;

import com.gao.springjdbc.domain.User3;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/24.
 */
public class SpringJDBCTest extends TestCase {
    private ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("com/gao/springjdbc/applicationContext-jdbc.xml");
    }
    /*测试jdbc查询*/
    public void test01(){
        DataSource dataSource= (DataSource) context.getBean("dataSource");
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Connection connection=dataSource.getConnection();
            String sql="select * from user3";
            ps=connection.prepareStatement(sql);
            rs=ps.executeQuery();
            List<User3> list=new ArrayList();
            while(rs.next()){
                User3 user=new User3();
                user.setId(rs.getInt("id"));
                user.setSalary(rs.getInt("salary"));
                user.setName(rs.getString("name"));
                user.setSex(rs.getString("sex"));
                list.add(user);
            }
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*插入用户*/
    public void testJDBCTemplete01(){
        JdbcTemplate template= (JdbcTemplate) context.getBean("jdbcTemplate");
        String sql="insert into user3 values (null,?,?,?)";
        int row=template.update(sql,"西施",4000,"女");
        System.out.println(row);
    }
    /*更新操作*/
    public void testJDBCTemplete02(){
        JdbcTemplate template= (JdbcTemplate) context.getBean("jdbcTemplate");
        String sql="update user3 set salary=? WHERE name=?";
        int row=template.update(sql,5000,"貂蝉");
        System.out.println(row);
    }
    /*删除操作*/
    public void testJDBCTemplate03(){
        JdbcTemplate template= (JdbcTemplate) context.getBean("jdbcTemplate");
        String sql="delete from user3 where name=?";
        int row=template.update(sql,"西施");
        System.out.println(row);
    }
    /*查询操作*/
    public void testJDBCTemplate04(){
        JdbcTemplate template= (JdbcTemplate) context.getBean("jdbcTemplate");
        String sql="select * from user3 WHERE id=?";
        RowMapper<User3> mapper= new RowMapper() {
            public User3 mapRow(ResultSet rs, int i) throws SQLException {
                User3 user=new User3();
                user.setId(rs.getInt("id"));
                user.setSalary(rs.getInt("salary"));
                user.setName(rs.getString("name"));
                user.setSex(rs.getString("sex"));
                return user;
            }
        };
        List<User3> list=template.query(sql,mapper,2);
        System.out.println(list);
    }
}
