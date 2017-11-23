package cn.gao.domain;

/**
 * Created by gao on 2017/1/31.
 */
public class FluxInfo {
    private String time;
    private String uv_id;
    private String ss_id;
    private String ss_time;
    private String urlname;
    private String cip;

    public FluxInfo(){}

    public FluxInfo(String time, String uv_id, String ss_id, String ss_time, String urlname, String cip) {
        this.time = time;
        this.uv_id = uv_id;
        this.ss_id = ss_id;
        this.ss_time = ss_time;
        this.urlname = urlname;
        this.cip = cip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUv_id() {
        return uv_id;
    }

    public void setUv_id(String uv_id) {
        this.uv_id = uv_id;
    }

    public String getSs_id() {
        return ss_id;
    }

    public void setSs_id(String ss_id) {
        this.ss_id = ss_id;
    }

    public String getSs_time() {
        return ss_time;
    }

    public void setSs_time(String ss_time) {
        this.ss_time = ss_time;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }
}
