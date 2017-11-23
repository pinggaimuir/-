package cn.shoparound.service;

import cn.shoparound.pojo.Product;

import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
public interface TbService {
    /**
     * 根据关键词搜索淘宝商场
     * @param keyword 关键词
     * @return
     */
    List<Product> searchTb(String keyword) throws Exception;

    /**
     * 根据关键词,和页数搜索淘宝商场
     * @param keyword 关键词
     * @return
     */
    List<Product> searchTbNext(String keyword,int page) throws Exception;
}
