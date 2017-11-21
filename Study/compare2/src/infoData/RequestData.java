package infoData;

/**
 * Created by Sammy on 14-5-10.
 */
public class RequestData {
    public String key_gbk;
    public String key_utf8;
    /**
     * 京东排序，京东网以参数psort作为排序字段。1-价格降序，2-价格升序，3-销量降序，……
     */
    public int jdsort;
    /**
     * 亚马逊排序，亚马逊排序字段为sort,sort=price-asc-rank是按价格由低到高排序,sort=price-desc-rank是按价格由高到低排序
     */
    public String azsort;

}
