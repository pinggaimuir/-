package cn.tedu.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自己做的QueryRunner
 * Created by tarena on 2016/9/5.
 */
public class  MyQueryRunner {
    private DataSource dataSource=null;
    public MyQueryRunner(DataSource dataSource){
        this.dataSource=dataSource;
    }
    /**/
    public Number update(String sql,Object...params){
        Connection conn=null;
        PreparedStatement stat=null;
        try {
            conn=dataSource.getConnection();
            stat=conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                stat.setObject(i+1,params[i]);
            }
            return (Number)stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
             try {
                if(stat!=null) stat.close();
                if(conn!=null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public <T>T query(String sql,MyResultSetHandler<T> rs,Object...params){
        Connection conn=null;
        PreparedStatement stat=null;
        ResultSet result=null;
        try {
            conn=dataSource.getConnection();
            stat=conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                stat.setObject(i+1,params[i]);
            }
            result=stat.executeQuery();
            return rs.handler(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
           {
                try {
                    if(result!=null) result.close();
                    if(stat!=null) stat.close();
                    if(conn!=null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
