package com.chenbingxian.dao;

import com.chenbingxian.po.Setholidays;

public interface SetholidaysMapper {
    int deleteByPrimaryKey(Integer holidaysid);

    int insert(Setholidays record);

    int insertSelective(Setholidays record);

    Setholidays selectByPrimaryKey(Integer holidaysid);

    int updateByPrimaryKeySelective(Setholidays record);

    int updateByPrimaryKey(Setholidays record);
}