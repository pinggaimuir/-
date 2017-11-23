package cn.gao.dao;

import cn.gao.domain.ResultInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by gao on 2017/2/3.
 */
public class MysqlDao {
    private MysqlDao(){}
    public static void insert(ResultInfo ri){
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://192.168.8.151:3306/flux","root","root");

            ps=conn.prepareStatement("insert into flux values(null,?,?,?,?,?,?)");

            ps.setDate(1,ri.getTime());
            ps.setInt(2,ri.getPv());
            ps.setInt(3, ri.getUv());
            ps.setInt(4, ri.getVv());
            ps.setInt(5, ri.getNewip());
            ps.setInt(6, ri.getNewcust());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            if(ps!=null){
                try{
                    ps.close();
                }catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }finally {
                    ps=null;
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    conn = null;
                }
            }

        }
    }
}
