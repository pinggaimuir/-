package cn.gao.hbase;

import java.sql.*;

/**
 * Created by gao on 2016/12/22.
 */
public class Phoenix {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        Connection conn= DriverManager.getConnection("jdbc:phoenix:hadoop");
        Statement stat=conn.createStatement();
        ResultSet rs=stat.executeQuery("SELECT  * FROM TAB1");
        while(rs.next()){
            System.out.println(rs.getString("col1"));
        }
        stat.close();
        conn.close();
    }
}
