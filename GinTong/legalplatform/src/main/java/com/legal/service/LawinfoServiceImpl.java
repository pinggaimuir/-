package com.legal.service;

import com.legal.commons.PageBean;
import com.legal.dao.SolrDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 高健 on 2017/3/11.
 */
@Service
public class LawinfoServiceImpl implements LawInfoService {

    @Resource
    private SolrDao solrDao;

//    public static void main(String[] args) {
//        SolrDao dao=new SolrDao();
//        List<WuSongInfo> items= dao.search("余忠华",1,10);
//        for(WuSongInfo info:items){
//            System.out.println(info.toString());
//        }
//    }

    /**
     * 根据关键词进行全文检索
     * @param keyWord 关键词
     * @param page 页数
     * @param rows 每页返回记录数
     */
    public PageBean search(String keyWord,Integer page,Integer rows){
        PageBean bean= solrDao.search(keyWord,page,rows);
        return bean;
    }
}
