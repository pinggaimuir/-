package cn.gao.service;

import cn.gao.commom.EasyUiResult;
import cn.gao.mapper.ItemsMapper;
import cn.gao.mapper.ItemsMapperCustom;
import cn.gao.po.Items;
import cn.gao.po.ItemsCustom;
import cn.gao.po.ItemsExample;
import cn.gao.po.ItemsQueryVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/6.
 */
@Service()
public class ItemsServiceImpl implements ItemsService{
    @Resource
    private ItemsMapperCustom itemsMapperCustom;
    @Resource
    private ItemsMapper itemsMapper;
    /*商品列表查询*/
    public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception {
        return itemsMapperCustom.findItemsList(itemsQueryVo);
    }
    /*根据id查询商品信息*/
    public ItemsCustom findItemsById(Integer id) throws Exception {
        Items items=itemsMapper.selectByPrimaryKey(id);
        //对商品信息业务处理
        ItemsCustom itemsCustom=new ItemsCustom();
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
        if(items!=null){
            BeanUtils.copyProperties(itemsCustom,items);
        }
        return itemsCustom;
    }
    /*根据id，商品信息修改商品信息*/
    public void updateItemsById(Integer id,ItemsCustom itemsCustom) throws Exception {
        //使用updateByPrimaryKeyWithBLOBs可以更新items表中的的所有包括大文本数据
        //在用这个方法前 要保证有id
        itemsCustom.setId(id);
        itemsMapper.updateByPrimaryKeyWithBLOBs(itemsCustom);
    }

    /*查询商品列表 结果为easyUI结果集*/
    public EasyUiResult getEasyUiItemList(int total, int rows) throws Exception {
        ItemsExample example=new ItemsExample();
        //分页处理
        PageHelper.startPage(total,rows);
        List<Items> itemsList=itemsMapper.selectByExample(example);
        //返回值对象
        EasyUiResult result=new EasyUiResult();
        result.setRows(itemsList);
        //取出记录总条数
        PageInfo<Items> info=new PageInfo<Items>(itemsList);
        result.setTotal(info.getTotal());
        return result;
    }
}
