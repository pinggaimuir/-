package August_23;

import java.sql.Connection;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCmyConection  {
    private Connection conn=null;
    private  JDBCPool pool =null;
    public JDBCmyConection(Connection conn,JDBCPool pool){
        this.conn=conn;
        this.pool=pool;
    }

}
