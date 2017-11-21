/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infoData;

/**
 *用于存储分页信息
 * @author MZH
 */
public class PageInfoData {
    /**
     * 当前正在显示页的页码
      */
  public int page;
    /**
     *京东总页数
     */
  public int jdpage;
    /**
     *亚马逊总页数
     */
  public int azpage;
    /**
     *亚马逊总页数
     */
  public int tbpage;
    /**
     * 搜索关键词字符串
     */
  public String key;
    /**
     * gbk格式的搜索关键词字符串
     */
  public String key_gbk;
    /**
     * utf-8格式的搜索关键词字符串
     */
  public String key_utf8;
    /**
     * 商家,1-从该商家获取商品信息，0-不获取该商家的商品信息
     */
    public int getFromB2C;
    public int getFromJd;
    public int getFromAz;
    public int getFromTb;
    /**
     * 是否通过价格排序,0-否，1-是
     */
    public int sortByPrice;

}
