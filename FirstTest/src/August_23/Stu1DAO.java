package August_23;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by tarena on 2016/8/23.
 */
public class Stu1DAO {
    /**
     * 查询stu1表总的数据
     * @return
     */
    public ArrayList<Stu1> getStu1(){
        ArrayList<Stu1> arr=new ArrayList<Stu1>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=JDBCUtils.getConnection();
            String sql="select * from stu1";
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                Stu1 stu1=new Stu1();
                stu1.setId(rs.getLong(1));
                stu1.setName(rs.getString(2));
                stu1.setDormitory(rs.getString(3));
                stu1.setCard(rs.getLong(4));
                arr.add(stu1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
           JDBCUtils.close(rs,ps,conn);
        }
        return arr;
    }

  /*  public static void main(String[] args) {
        Stu1DAO s=new Stu1DAO();
        List<Stu1> list=s.getStu1();
        for(Stu1 s1:list){
            System.out.println(s1);
        }
    }*/

    /**
     * 向stu1中插入记录
     * @param arr
     */
    public void insertData(ArrayList<Stu1> arr){
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=JDBCUtils.getConnection();
            String sql="insert into stu1 values(?,?,?,?) ";
            ps=conn.prepareStatement(sql);
            for(Stu1 stu1:arr){
                ps.setLong(1,stu1.getId());
                ps.setString(2,stu1.getName());
                ps.setString(3,stu1.getDormitory());
                ps.setLong(4,stu1.getCard());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(null,ps,conn);
        }
    }

    /**
     * 通过id删除记录
     * @param id
     */
    public void deleteData(Long id){
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=JDBCUtils.getConnection();
            String sql="delete from stu1 where id=?";
            ps=conn.prepareStatement(sql);
            ps.setLong(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(null,ps,conn);
        }
    }
}
