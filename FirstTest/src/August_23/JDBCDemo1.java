package August_23;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.sql.*;

/**
 * JDBC连接测试
 * Created by tarena on 2016/8/22.
 */
public class JDBCDemo1
{
    public static void main(String[] args){
        Connection conn=null;
        Statement state=null;
        try{
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //建立连接
            conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb2","root","root");
            //建立执行SQL语句的Statement对象
            state=conn.createStatement();
            //查询
            String sql="select *  from employee";
            ResultSet rs=state.executeQuery(sql);
            System.out.println("用户名\t\t密码");
            while(rs.next()){
                System.out.println(rs.getString(1)+"\t\t"+rs.getString(2));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                state.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void find(){
        Connection conn=null;
        Statement stat=null;
        ResultSet rs=null;

        try {
            conn=BaseConnection.getConnection();
            stat=conn.createStatement();
            String sql="select * from employee";
            rs=stat.executeQuery(sql);
            while(rs.next()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
