package cn.shoparound.service;

import cn.shoparound.pojo.Product;

import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
public interface AzService {
    /**
     * 根据关键词搜索亚马逊商场
     * @param keyword 关键词
     * @return
     */
    List<Product> searchAz(String keyword) throws Exception;

    /**
     * 根据关键词,和页数搜索亚马逊商场
     * @param keyword 关键词
     * @return
     */
    List<Product> searchAzNext(String keyword,int page) throws Exception;
}
