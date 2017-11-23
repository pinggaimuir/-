package cn.shoparound.service.impl;

import cn.shoparound.pojo.Product;
import cn.shoparound.service.JdService;
import cn.shoparound.spider.JdSpider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
@Service
public class JdServiceImpl implements JdService {
    @Resource
    private JdSpider jdSpider;

    public List<Product> searchJd(String keyword) throws Exception {
        List<Product> jdList=jdSpider.getProductInfo(keyword);
        return jdList;
    }

    /**
     * 根据关键词,和页数搜索京东商场
     *
     * @param keyword 关键词
     * @param page
     * @return
     */
    public List<Product> searchJdNext(String keyword, int page) throws Exception {
        List<Product> jdList=jdSpider.nextPage(keyword,page);
        return jdList;
    }
}
