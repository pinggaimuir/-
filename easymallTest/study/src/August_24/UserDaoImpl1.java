package August_24;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/8/24.
 */
public class UserDaoImpl1 implements UserDao1 {
    /**
     *保存用户信息
     */
    @Override
    public void save(Connection conn, User1 user) throws SQLException {
        String sql="Insert into tbl_user(name,password,email)values(?,?,?)";
        PreparedStatement ps=conn.prepareCall(sql);
        ps.setString(1,user.getName());
        ps.setString(2,user.getPassword());
        ps.setString(3,user.getEmail());
        ps.execute();
    }

    /**
     *根据用户制定的id跟新用户信息
     */
    @Override
    public void update(Connection conn, long id, User1 user) throws SQLException {
        String updateSql="update tbl_user set name=?,password=?,email=? where id=?";
        PreparedStatement ps=conn.prepareCall(updateSql);
        ps.setString(1,user.getName());
        ps.setString(2,user.getPassword());
        ps.setString(3,user.getEmail());
        ps.setLong(4,id);
        ps.execute();
    }

    /**
     *删除制定的用户信息
     */
    @Override
    public void delete(Connection conn, User1 user) throws SQLException {
        String deleteSql="delete from tbl_user where id=?";
        PreparedStatement ps=conn.prepareCall(deleteSql);
        ps.setLong(1,user.getId());
        ps.execute();
    }

    @Override
    public ResultSet get(Connection conn, User1 user) throws SQLException {
        String sql="select * FROM tbl_user where name=? AND  password=?";
        PreparedStatement ps=conn.prepareCall(sql);
        ps.setString(1,user.getName());
        ps.setString(2,user.getPassword());
        return ps.executeQuery();
    }
}
