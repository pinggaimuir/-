package August_23;

import java.sql.*;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCStatementbatch {
    public static void main(String[] args) {
        Connection conn=null;
        Statement stat=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb2","root","root");
            stat=conn.createStatement();
            for(int i=0;i<500;i++){
                stat.addBatch("insert into user values('name"+i+"','pwd"+i+"')");
                if(i%100==0){
                    stat.executeBatch();
                    stat.clearBatch();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            //BaseConnection.close(conn,stat,rs);
        }
    }
}
