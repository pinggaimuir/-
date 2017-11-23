/*package Semptemble_10;

import java.util.ArrayList;

*//**
 * Created by tarena on 2016/9/18.
 *//*
public class fen {
    public List<Bar> pageListOne(int currentPage,int showRows) {
        List<Bar> result = new ArrayList();
        int skipBigin = (currentPage - 1) * showRows;
        int skipEnd = currentPage * showRows;
        int currentNum = 0;
        while (rs.next()) {
            if (currentNum >= skipBigin & currentNum < skipEnd) {
                Bar bar = new Bar();
                bar.setId(rs.getLong());
                bar.setName(rs.getSring());
                bar.setPassword(rs.getString());
                result.add(bar);
                if (currentNum == skipEnd - 1) break;
            }
            currentNum++;
        }
        return result;
    }
    //第二种方式
    public List<Bar> pageListTwo(int currentPage,int showRows) {
        List<Bar> result = new ArrayList();
        int skipBigin = (currentPage - 1) * showRows;
        int skipEnd = currentPage * showRows;
        if(!rs.absolute(skipBigin))return result;
        while(rs.next()){
            if(skipBigin<skipEnd){
                Bar bar=new Bar();
                result.add(bar);
                if(skipBigin==skipEnd-1)break;
            }
        }

        return result;
    }
    public List<Bar> pageListTwo2(int thispage,int rowpage){
        List<Bar> list=new ArrayList();
        int begin=(thispage-1)*rowpage;
        int end=thispage*rowpage;
        int count=0;
        while(rs.next){
            if(count>=begin&&count<end){
                Bar bar=new Bar();
                bar.set(rs.getString(1));
                list.add(bar);
                if(count==end-1)break;
            }
            count++;
        }
        return list;
    }
}*/
