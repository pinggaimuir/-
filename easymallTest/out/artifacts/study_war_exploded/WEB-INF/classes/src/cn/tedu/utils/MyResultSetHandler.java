package cn.tedu.utils;

import java.sql.ResultSet;

/**
 * Created by tarena on 2016/9/5.
 */
public interface MyResultSetHandler<T> {
    T handler(ResultSet rs)throws  Exception;
}
