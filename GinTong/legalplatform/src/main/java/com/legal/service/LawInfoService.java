package com.legal.service;

import com.legal.commons.PageBean;

/**
 * Created by 高健 on 2017/3/12.
 */
public interface LawInfoService {
    PageBean search(String keyWord, Integer page, Integer rows);
}