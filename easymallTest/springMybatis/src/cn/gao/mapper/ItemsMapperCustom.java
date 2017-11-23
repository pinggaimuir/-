package cn.gao.mapper;

import cn.gao.po.ItemsCustom;
import cn.gao.po.ItemsQueryVo;

import java.util.List;

/**
 * Created by tarena on 2016/10/6.
 */
public interface ItemsMapperCustom {
    /*查询商品列表*/
    List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)throws Exception;
}
