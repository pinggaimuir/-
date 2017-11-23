package com.legal.commons;

import com.legal.pojo.WuSongInfo;

import java.util.List;

/**
 * Created by 高健 on 2017/3/12.
 */
public class PageBean {
    private List<WuSongInfo> data;//查询出的数据
    private long total;//查询出的总记录数
    private Integer page;//当前页

    public List<WuSongInfo> getData() {
        return data;
    }

    public void setData(List<WuSongInfo> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {

        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
