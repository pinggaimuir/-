package cn.gao.domain;

import java.sql.Date;

/**
 * Created by gao on 2017/1/31.
 */
public class ResultInfo {
    private Date time;
    private int pv;
    private int uv;
    private int vv;
    private int newip;
    private int newcust;

    public ResultInfo(){}

    public ResultInfo(Date time, int pv, int uv, int vv, int newip, int newcust) {
        this.time = time;
        this.pv = pv;
        this.uv = uv;
        this.vv = vv;
        this.newip = newip;
        this.newcust = newcust;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getVv() {
        return vv;
    }

    public void setVv(int vv) {
        this.vv = vv;
    }

    public int getNewip() {
        return newip;
    }

    public void setNewip(int newip) {
        this.newip = newip;
    }

    public int getNewcust() {
        return newcust;
    }

    public void setNewcust(int newcust) {
        this.newcust = newcust;
    }
}
