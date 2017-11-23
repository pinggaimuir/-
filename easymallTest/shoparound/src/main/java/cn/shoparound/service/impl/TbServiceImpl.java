package cn.shoparound.service.impl;

import cn.shoparound.pojo.Product;
import cn.shoparound.service.TbService;
import cn.shoparound.spider.TbSpider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
@Service
public class TbServiceImpl implements TbService {

    @Resource
    private TbSpider tbSpider;

    public List<Product> searchTb(String keyword) throws Exception {
        List<Product> tblist=tbSpider.getProductInfo(keyword);
        return tblist;
    }

    /**
     * 根据关键词,和页数搜索淘宝商场
     *
     * @param keyword 关键词
     * @param page
     * @return
     */
    public List<Product> searchTbNext(String keyword, int page) throws Exception {
        List<Product> tblist=tbSpider.nextPage(keyword,page);
        return tblist;
    }
}
