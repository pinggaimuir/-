package test2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/5.
 */
public class ResultHandler {
    public static void main(String[] args) {
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        QueryRunner qr=new QueryRunner(dataSource);
        Connection conn=null;
        try {
             conn=dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql="select * from user";
        Object[] params={"gao"};
        try {
            List<User2> list= qr.query(sql,new BeanListHandler<User2>(User2.class));
            for(User2 user2:list){
                System.out.println(user2);
            }
          } catch (SQLException e) {
            e.printStackTrace();
        }
/*        String sql="delete from user where username=?";
        Object[] params={"悟空"};
        try {
            int i=qr.update(conn,sql,params);
            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{

        }*/


    }
}
