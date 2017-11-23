package com.gao.practice;

/**
 * Created by tarena on 2016/9/21.
 */
//public class logicfen {
//    public List<Bar> fenye(int currentPage,int showPage){
//        List<Bar> barlist=new ArrayList();
//        int start=(currentPage-1)*showPage;
//        int end=currentPage*showPage;
//
//        int count=0;
//        while(rs.next()){
//            if(count>=start&&count<end){
//                Bar bar=new Bar();
//                bar.setName(rs.getString(1));
//                bar.add(bar);
//                if(count==end-1)break;
//            }
//            count++;
//        }
//        return barlist;
//    }
//
//
//
//    public List<Bar> fenye(int currentPage,int showPage){
//        List<Bar> barlist=new ArrayList();
//        int begin=(currentPage-1)*showPage;
//        int end=currentPage*showPage;
//
//        if(!rs.absolute(begin))return barlist;
//        while(rs.next()){
//            if(begin<end){
//                Bar bar=new Bar();
//                bar.setName(rs.getString(1));
//                bar.add(bar);
//                if(begin==end-1)break;
//            }
//        }
//        return barlist;
//    }
//}
