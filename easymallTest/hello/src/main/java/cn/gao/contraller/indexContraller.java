package cn.gao.contraller;

import cn.gao.commom.EasyUiResult;
import cn.gao.service.ItemsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/10/11.
 */
@Controller
public class indexContraller {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @Resource
    private ItemsService itemsService;

    @RequestMapping("/item/itemlist/{page}/{size}")
    @ResponseBody
    public EasyUiResult getEasyUiResult(@PathVariable Integer page, @PathVariable Integer size) throws Exception {
        EasyUiResult result=itemsService.getEasyUiItemList(page,size);
        return result;
    }
}
