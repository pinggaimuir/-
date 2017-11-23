package cn.gao.service;

import cn.gao.commom.EasyUiResult;
import cn.gao.mappers.ItemsMapper;
import cn.gao.pojo.Items;
import cn.gao.pojo.ItemsExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/12.
 */
@Service
public class ItemsServiceImpl implements ItemsService{
    @Resource
    private ItemsMapper itemsMapper;
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
        result.setTotal(66);
        return result;
    }
}
