package com.gao.beantest1;

/**
 * Created by tarena on 2016/9/20.
 */
public class BaseDao {
    public BaseDao(){
    }
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
