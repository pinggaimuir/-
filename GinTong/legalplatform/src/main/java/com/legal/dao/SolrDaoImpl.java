package com.legal.dao;

import com.legal.commons.PageBean;
import com.legal.pojo.WuSongInfo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by 高健 on 2017/3/11.
 */
@Repository
public class SolrDaoImpl implements SolrDao{
    @Resource
    private HttpSolrServer httpSolrServer;

    /**
     * 根据关键词从solr中查询记录
     * @param keyWords 关键词
     * @param page 当前页数
     * @param rows 每页记录数
     */
    public PageBean search(String keyWords, Integer page, Integer rows){
        //构造搜索对象
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery(keyWords);
        //设置分页
        solrQuery.setStart((Math.max(1,page)-1)*rows);
        solrQuery.setRows(rows);
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<span class=\"red\">");
        solrQuery.setHighlightSimplePost("</span>");
        solrQuery.addHighlightField("title");

        try {
            QueryResponse queryResponse=httpSolrServer.query(solrQuery);
            List<WuSongInfo> items=queryResponse.getBeans(WuSongInfo.class);
            SolrDocumentList docs = queryResponse.getResults();
            if(items==null||items.isEmpty()){
//                return SysResult.build(200,"没有搜索到数据！");
                System.out.println("又有搜索到数据");
            }
//            Map<String,Map<String,List<String>>> map=queryResponse.getHighlighting();
//            for(Map.Entry<String,Map<String,List<String>>> entry:map.entrySet()){
//                for(Item item:items){
//                    if(!entry.getKey().equals(item.getId().toString())){
//                        continue;
//                    }
//                    item.setTitle(StringUtils.join(entry.getValue().get("title"),""));
//                    break;
//                }
//            }
//            return SysResult.build(200,String.valueOf(queryResponse.getResults().getNumFound()),items);
            long total=docs.getNumFound();

            PageBean bean=new PageBean();
            bean.setData(items);
            bean.setTotal(total%rows==0?total/rows:total/rows+1);
            bean.setPage(page);
            return bean;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
