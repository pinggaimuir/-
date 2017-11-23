package SpiderAZ;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 123 on 2017/2/18.
 */
public class BasProGameInfo implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGename() {
        return gename;
    }

    public void setGename(String gename) {
        this.gename = gename;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getSuitable_age() {
        return suitable_age;
    }

    public void setSuitable_age(String suitable_age) {
        this.suitable_age = suitable_age;
    }

    public String getIssue_area() {
        return issue_area;
    }

    public void setIssue_area(String issue_area) {
        this.issue_area = issue_area;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGstyle() {
        return gstyle;
    }

    public void setGstyle(String gstyle) {
        this.gstyle = gstyle;
    }

    public String getGtags() {
        return gtags;
    }

    public void setGtags(String gtags) {
        this.gtags = gtags;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getViewpoint() {
        return viewpoint;
    }

    public void setViewpoint(String viewpoint) {
        this.viewpoint = viewpoint;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDpprogress() {
        return dpprogress;
    }

    public void setDpprogress(String dpprogress) {
        this.dpprogress = dpprogress;
    }

    public String getGtheme() {
        return gtheme;
    }

    public void setGtheme(String gtheme) {
        this.gtheme = gtheme;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getPdemand() {
        return pdemand;
    }

    public void setPdemand(String pdemand) {
        this.pdemand = pdemand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(String charge_mode) {
        this.charge_mode = charge_mode;
    }

    public String getDevelop_com() {
        return develop_com;
    }

    public void setDevelop_com(String develop_com) {
        this.develop_com = develop_com;
    }

    public String getG_desc() {
        return g_desc;
    }

    public void setG_desc(String g_desc) {
        this.g_desc = g_desc;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public Date getBeta_tume() {
        return beta_tume;
    }

    public void setBeta_tume(Date beta_tume) {
        this.beta_tume = beta_tume;
    }

    public Date getTest_time() {
        return test_time;
    }

    public void setTest_time(Date test_time) {
        this.test_time = test_time;
    }

    public Date getBetatest_time() {
        return betatest_time;
    }

    public void setBetatest_time(Date betatest_time) {
        this.betatest_time = betatest_time;
    }

    public Date getSet_time() {
        return set_time;
    }

    public void setSet_time(Date set_time) {
        this.set_time = set_time;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



    public String getGamespy() {
        return gamespy;
    }

    public void setGamespy(String gamespy) {
        this.gamespy = gamespy;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFilms_time() {
        return films_time;
    }

    public void setFilms_time(String films_time) {
        this.films_time = films_time;
    }

    public String getGame_size() {
        return game_size;
    }

    public void setGame_size(String game_size) {
        this.game_size = game_size;
    }

    public String getWeb_update_time() {
        return web_update_time;
    }

    public void setWeb_update_time(String web_update_time) {
        this.web_update_time = web_update_time;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    private String id;//(主键)
    private String gname;////(游戏名)
    private String gename;//(游戏英语名字)
    private String logo;//(产品logo)
    private String version;//(游戏版本)
    private String language;//(语言)
    private String network_type;//(0未知，1不需要联网，2需要联网)
    private String suitable_age;//(适合年龄)
    private String issue_area;//(发行区域)
    private String publisher;//(发行商)
    private String gstyle;//(游戏风格)
    private String gtags;//(标签)
    private String picture;//(游戏截图（逗号隔开）)
    private String viewpoint;//(游戏视角)
    private String engine;//(游戏引擎)
    private String grade;//(游戏品级)
    private String dpprogress;//(研发进度)
    private String gtheme;//(游戏题材)
    private String scale;//(产品团队规模)
    private String pdemand;//(产品需求)
    private String price;//(价格)
    private String charge_mode;//(付费方式)
    private String develop_com;//(研发公司)
    private String g_desc;//(游戏介绍)
    private String ptime;//(出版出售时间)
    private Date beta_tume;//(公测时间)
    private Date test_time;//(内测时间)
    private Date betatest_time;//(封测时间)
    private Date set_time;//(立项时间)
    private String web;//(游戏官网)
    private String url;//
    private String source;//
    private String uuid;//
    private String gamespy;//(游戏画面（2D，3D）)
    private String operator;//(运营商)
    private String films_time;//(资料片发布时间)
    private String game_size;//(游戏大小)
    private String web_update_time;//(网站更新时间)
    private String download_link;//(下载链接)


}
