package August_23;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBClogin2 {
    public static void main(String[] args) {
        System.out.println("请登录：");
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username=sc.nextLine();
        System.out.println("请输入密码：");
        String password=sc.nextLine();
        login(username,password);
    }
    private static void login (String username,String password){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb2","root","root");
            String sql="select * from user where username=? and password=?";
            ps=  conn.prepareStatement(sql);
          //  rs=stat.executeQuery("select *  from user where username='"+username+"'and password='"+password+"'");
            ps.setString(1,username);
            ps.setString(2,password);
            rs=ps.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString(1)+"--------登陆成功");
            }else{
                System.out.println("登陆失败！");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            BaseConnection.close(conn,ps,rs);
        }
    }
}
