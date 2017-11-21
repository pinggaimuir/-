package spider;

import pojo.Item;
import pojo.PageInfo;

import java.util.List;

/**
 * 爬虫接口
 * Created by gao on 2016/11/26.
 */
public interface SpiderInterface {
    //获取商品列表
    List<Item> getItemList(String keyWord)throws Exception;
}
