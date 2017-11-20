package August_23;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by tarena on 2016/8/23.
 */
public class NewsDAO {

    public ArrayList<News> getList(){
        ArrayList<News> ar=new ArrayList<News>();
        Connection conn=BaseConnection.getConnection();
        //SQL执行器对象
        PreparedStatement ps=null;
        //结果集对象
        ResultSet rs=null;;
        try {
            String sql="select *  from xinwen,newstype where xinwen.type=newstype.id";
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                News ne=new News();
                ne.setId(rs.getInt("id"));
                ne.setTitle(rs.getString("title"));
                ne.setType(rs.getInt("type"));
                ne.setContent(rs.getString("content"));
                ne.setName(rs.getString("name"));
                ar.add(ne);
                //System.out.println(rs.getLong("id")+"-----"+rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            BaseConnection.close(conn,ps,rs);
        }
        return ar;
    }
    /*测试函数*/
    public static void main(String[] args) {
       ArrayList<News> ar= new NewsDAO().getList();
        for(News ne:ar){
            System.out.println(ne.getId()+"---"+ne.getTitle()+"----"+ne.getName());
        }
    }
}
