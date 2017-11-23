package August_24;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 根据用户的登陆信息验证信息
 * Created by tarena on 2016/8/24.
 */
public class CheckUserService {
    private UserDao1 userDao=new UserDaoImpl1();
    public boolean check(User1 user){
        Connection conn=null;
        ResultSet rs=null;
        try {
            Class.forName("com.jdbc.mysql.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb2","root","root");
            conn.setAutoCommit(false);
            rs=userDao.get(conn,user);
            while(rs.next()){
                return true;
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
        }
        return false;
    }
}
