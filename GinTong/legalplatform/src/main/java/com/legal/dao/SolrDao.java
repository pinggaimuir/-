package com.legal.dao;

import com.legal.commons.PageBean;

/**
 * Created by 高健 on 2017/3/12.
 */
public interface SolrDao {
    PageBean search(String keyWords, Integer page, Integer rows);
}

