package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by gao on 2016/8/28.
 */
public class JDBCUtils {
    private JDBCUtils(){
    }
    public Connection getConnection(){
        Connection conn=null;
        //Statement stat=null;
        try{
            Class.forName("com.jdbc.mysql.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
