package cn.shoparound.spider;

import cn.shoparound.pojo.Product;

import java.util.List;

/**
 * Created by 高健 on 2017/5/17.
 */
public interface Spider {

    /**
     * 根据关键词爬取商品信息列表
     * @param keyword
     * @return
     */
    List<Product> getProductInfo(String keyword) throws Exception;
}
