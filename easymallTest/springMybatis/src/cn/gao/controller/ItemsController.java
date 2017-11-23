package cn.gao.controller;

import cn.gao.commom.EasyUiResult;
import cn.gao.exception.CustomException;
import cn.gao.po.ItemsCustom;
import cn.gao.po.ItemsQueryVo;
import cn.gao.service.ItemsService;
import cn.gao.validator.Validator1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by tarena on 2016/10/6.
 */
@Controller
@RequestMapping("/item")
public class ItemsController {
    @Resource
    private ItemsService itemsService;
    /* 首页跳转 */

    /*显示商品列表*/
    @RequestMapping(value="/queryItems",method={RequestMethod.GET})//窄化请求路径，闲限制请求方法
    public ModelAndView queryItems(ItemsQueryVo itemsQueryVo)throws Exception{
        List<ItemsCustom> itemsList=itemsService.findItemsList(itemsQueryVo);

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("itemsList",itemsList);
        modelAndView.setViewName("items/itemsList");
        return modelAndView;
    }
    /*显示商品修改页面*/
    @RequestMapping("/editItems/{id}")
    public ModelAndView queryItems(@PathVariable Integer id)throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        ItemsCustom itemsCustom=itemsService.findItemsById(id);
        if(itemsCustom.getId()==null){
            throw new CustomException("该商品信息不存在！");
        }
        modelAndView.addObject("items",itemsCustom);
        modelAndView.setViewName("items/editItems");
        return modelAndView;
    }
    /*商品修改信息的提交*/
    @RequestMapping("/editItemsSubmit")
    public String editItemsSubmit(Model model,@Validated(value={Validator1.class})
                                    @ModelAttribute("items") ItemsCustom itemsCustom,
                                  BindingResult result,
                                  @RequestParam("picFile") MultipartFile picFile)throws Exception{
        //捕获错误
        if(result.hasErrors()){
            List<ObjectError> allerrors=result.getAllErrors();
            for(ObjectError error:allerrors){
                System.out.println(error.getDefaultMessage());
            }
            //数据回显
            model.addAttribute("allErrors",allerrors);
            return "items/editItems";
        }
        if(picFile!=null){
            String picOldName=picFile.getOriginalFilename();
            String picNewName= UUID.randomUUID().toString()+picOldName.substring(picOldName.lastIndexOf("."));
            File pic=new File("D:/upload/pic/"+picNewName);
            if(!pic.exists()){
                pic.mkdirs();
            }
            //将新图片写入硬盘
            picFile.transferTo(pic);
            //将新图片路径写入数据库
            itemsCustom.setPic(picNewName);
        }
        //修改数据库
        itemsService.updateItemsById(itemsCustom.getId(),itemsCustom);

        return "success";
    }
    @RequestMapping("/itemlist/{page}/{rows}")
    @ResponseBody
    public EasyUiResult getItemEasyUiList(@PathVariable Integer page,@PathVariable Integer rows) throws Exception {
        EasyUiResult result=itemsService.getEasyUiItemList(page,rows);
        return result;
    }
}
