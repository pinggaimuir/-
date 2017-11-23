package cn.gao.commom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tarena on 2016/10/21.
 */
public class InsertTest {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        long begin=System.currentTimeMillis();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url="jdbc:oracle:thin:@127.0.0.1:1521:XE";
            conn= DriverManager.getConnection(url,"test1","test1");
            conn.setAutoCommit(false);
            ps=conn.prepareStatement("INSERT INTO USER_TEST VALUES(?,?,?,?)");
            int count=0;
            int num=0;
            while(true){
                count++;
                num++;
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2,"user"+num);
                ps.setString(3,"pwd"+num);
                ps.setDate(4,new java.sql.Date(new Date().getTime()));
                ps.addBatch();
                //每插入50000条数据提交一次
                if(num>50000){
                    ps.executeBatch();
                    conn.commit();
                    num=0;
                }
                //插入数据到大1000000则停止循环
                if(count==1000000){

                    break;
                }
                ps.executeBatch();
                conn.commit();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis()-begin);
        }
    }
}
