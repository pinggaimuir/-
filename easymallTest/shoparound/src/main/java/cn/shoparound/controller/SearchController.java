package cn.shoparound.controller;

import cn.shoparound.pojo.Product;
import cn.shoparound.service.AzService;
import cn.shoparound.service.JdService;
import cn.shoparound.service.TbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 高健 on 2017/5/19.
 */
@Controller
@RequestMapping("/shop")
public class SearchController {

    @Resource
    private JdService jdService;

    @Resource
    private TbService tbService;

    @Resource
    private AzService azService;

    @RequestMapping("/{keyword}/top")
    public String index(@PathVariable("keyword") String keyword,
                       Model model) throws Exception {
        model.addAttribute("keyword",keyword);
        return "top";
    }


    /**
     * 搜索三家商城的数据并且返回
     * @param keyword
     * @return
     */
    @RequestMapping("/{keyword}/skip")
    public String skip(@PathVariable("keyword") String keyword,
                         Model model) throws Exception {
        model.addAttribute("keyword",keyword);
        return "main";
    }

    /**
     * 搜索三家商城的数据并且返回
     * @param keyword
     * @return
     */
    @RequestMapping("/{keyword}/jd/search")
    public String searchJd(@PathVariable("keyword") String keyword,
                         Model model) throws Exception {
        List<Product> plist=jdService.searchJd(keyword);
        model.addAttribute("jdlist",plist);
        return "jdmain";
    }

    /**
     * 搜索三家商城的数据并且返回
     * @param keyword
     * @return
     */
    @RequestMapping("/{keyword}/tb/search")
    public String searchTb(@PathVariable("keyword") String keyword,
                         Model model) throws Exception {
        List<Product> plist=tbService.searchTb(keyword);
        model.addAttribute("tblist",plist);
        return "tbmain";
    }

    /**
     * 搜索三家商城的数据并且返回
     * @param keyword
     * @return
     */
    @RequestMapping("/{keyword}/az/search")
    public String search(@PathVariable("keyword") String keyword,
                         Model model) throws Exception {
        List<Product> plist = azService.searchAz(keyword);
        model.addAttribute("azlist", plist);
        return "azmain";
    }
    /**
     * 根据关键字获取第几页信息
     * @param keyword
     * @param page
     * @return
     */
    @RequestMapping("/{keyWord}/{page}/{mallInfo}/searchnext")
    public String jdSearchNext(@PathVariable("keyWord") String keyword,
                                      @PathVariable("page") Integer page,
                                      @PathVariable("mallInfo") String mallInfo,
                                      Model model) throws Exception {
        List<Product> plist=null;
        if("jd".equals(mallInfo)){
            plist=jdService.searchJdNext(keyword,page);
            model.addAttribute("jdlist",plist);
            return "jdmain";
        }
        if("az".equals(mallInfo)){
            plist=azService.searchAzNext(keyword,page);
            model.addAttribute("azlist",plist);
            return "azmain";
        }
        if("tb".equals(mallInfo)){
            plist=tbService.searchTbNext(keyword,page);
            model.addAttribute("tblist",plist);
            return "tbmain";
        }
//        model.addAttribute("keyWord",keyWord);
        return "";
    }

}
