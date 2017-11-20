package cn.tedu.utils;

import java.util.List;

/**
 * Created by tarena on 2016/9/7.
 */
public class Page<T> {
    private List<T> list;//当前页的结果集
    private int thispage;//当前第几页
    private int rowperpage; //每页的记录数
    private int countrow;//总共的记录数
    private int countpage;//页数纵隔
    private int prepage;//上一页
    private int nextpage;//下一页

    public int getCountpage() {
        return countpage;
    }

    public void setCountpage(int countpage) {
        this.countpage = countpage;
    }

    public int getCountrow() {
        return countrow;
    }

    public void setCountrow(int countrow) {
        this.countrow = countrow;
    }

    public int getNextpage() {
        return nextpage;
    }

    public void setNextpage(int nextpage) {
        this.nextpage = nextpage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPrepage() {
        return prepage;
    }

    public void setPrepage(int prepage) {
        this.prepage = prepage;
    }

    public int getThispage() {
        return thispage;
    }

    public void setThispage(int thispage) {
        this.thispage = thispage;
    }

    public int getRowperpage() {
        return rowperpage;
    }

    public void setRowperpage(int rowperpage) {
        this.rowperpage = rowperpage;
    }
}
