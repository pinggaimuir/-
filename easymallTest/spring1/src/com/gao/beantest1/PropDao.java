package com.gao.beantest1;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/20.
 */
@Repository("propDao")
public class PropDao {
    @Resource(name="dbutil")
    private JdbcUtil jdbcUtil;
    public String getUrl(){
        return jdbcUtil.getCon();
    }
}
