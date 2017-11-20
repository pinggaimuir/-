package August_23;

import java.util.ArrayList;

/**
 * Created by tarena on 2016/8/23.
 */
public class Stu1Servlet {
    public static void main(String[] args) {
        Stu1Servlet ss=new Stu1Servlet();
        ArrayList<Stu1> sl=new ArrayList();
        for (int i = 0; i <29 ; i++) {
            Stu1 stu1=new Stu1(20131103101l+i,"stu"+i,"文华苑3#",2013l);
            sl.add(stu1);
        }
       ss.insertStu1(sl);
//        for (int i = 0; i <29 ; i++) {
//            ss.deletetStu1(20131103101l+i);
//        }
        ss.selectStu1();
    }
    //查询整张stu1表
    public ArrayList<Stu1> selectStu1(){
        Stu1DAO sd=new Stu1DAO();
        ArrayList<Stu1> arr=sd.getStu1();
        for(Stu1 s:arr){
            System.out.println(s);
        }
        return arr;
    }
    //根据id 删除记录
    public void deletetStu1(Long id){
        Stu1DAO sd=new Stu1DAO();
        sd.deleteData(id);
        System.out.println("删除成功!");
    }
    //插入数据
    public void insertStu1(ArrayList<Stu1> arr){
        Stu1DAO sd=new Stu1DAO();
        sd.insertData(arr);
        System.out.println("插入成功！");
    }
}
