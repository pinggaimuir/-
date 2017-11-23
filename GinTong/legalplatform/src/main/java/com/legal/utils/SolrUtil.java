package com.legal.utils;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

/**
 * 获取SolrServer
 * Created by 高健 on 2017/3/11.
 */
public class SolrUtil {
    private SolrUtil(){}
    private static final HttpSolrServer httpSolrServer=new HttpSolrServer("http://hadoop:8983/solr/lawinfo");
    public static   HttpSolrServer getSolr(){
        httpSolrServer.setParser(new XMLResponseParser());
        httpSolrServer.setConnectionTimeout(500);
        httpSolrServer.setMaxRetries(1);
        return httpSolrServer;
    }

}
