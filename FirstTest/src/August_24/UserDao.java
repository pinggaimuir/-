package August_24;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/8/24.
 */
public interface UserDao {
    public void save(Connection conn,User user) throws SQLException;
    public void update(Connection conn,long id,User user)throws SQLException;
    public void delete(Connection conn,User user)throws SQLException;
    public ResultSet get(Connection conn,User user)throws SQLException;
}
