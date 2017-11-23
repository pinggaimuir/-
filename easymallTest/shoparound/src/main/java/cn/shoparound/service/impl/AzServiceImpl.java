package cn.shoparound.service.impl;

import cn.shoparound.pojo.Product;
import cn.shoparound.service.AzService;
import cn.shoparound.spider.AzSpider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
@Service
public class AzServiceImpl implements AzService {

    @Resource
    private AzSpider azSpider;

    public List<Product> searchAz(String keyword) throws Exception {
        List<Product> azlist=azSpider.getProductInfo(keyword);
        return azlist;
    }

    /**
     * 根据关键词,和页数搜索亚马逊商场
     *
     * @param keyword 关键词
     * @param page
     * @return
     */
    public List<Product> searchAzNext(String keyword, int page) throws Exception {
        List<Product> azlist=azSpider.nextPage(keyword,page);
        return azlist;
    }
}
