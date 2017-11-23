package cn.gao.service;

import cn.gao.commom.EasyUiResult;
import cn.gao.po.ItemsCustom;
import cn.gao.po.ItemsQueryVo;

import java.util.List;

/**
 * 商品管理service
 * Created by tarena on 2016/10/6.
 */
public interface ItemsService {
    /*查询商品列表*/
    List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)throws Exception;
    /*根据id查询商品信息*/
    ItemsCustom findItemsById(Integer id)throws Exception;
    /*根据id，商品信息修改商品信息*/
    void updateItemsById(Integer id,ItemsCustom itemsCustom)throws Exception;
    /*查询商品列表 结果为easyUI结果集*/
    EasyUiResult getEasyUiItemList(int page,int rows)throws Exception;
}
