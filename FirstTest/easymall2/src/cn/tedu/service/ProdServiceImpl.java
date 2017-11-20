package cn.tedu.service;

import cn.tedu.dao.ProdDao;
import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.utils.Page;

import java.util.List;

/**
 * Created by tarena on 2016/9/6.
 */
public class ProdServiceImpl implements ProdService {
    private  ProdDao pd= BasicFactory.getFactory().getInstance(ProdDao.class);
    /**
     * 添加商品的方法
     * @param prod 商品对象
     */
    public void addProduct(Product prod) {
        pd.addProduct(prod);
    }

    /**
     * 显示商品列表
     * @return 商品对象集合
     */
    public List<Product> prodList() {
        return pd.prodList();
    }


    public int deleteProd(String id) {
        int d=pd.deleteProd(id);
        if(d<0){
            throw new RuntimeException("删除的商品不存在");
        }
        return d;
    }

    /**
     * 根据商品id更改商品库存数量
     * @param id 商品id
     * @param pnum 更改的新数量
     */
    public void updatePnum(String id, int pnum) {
        pd.updatePnum(id,pnum);
    }
    /**
     * 带有查询条件的分页
     * @param thispage 往前第几页
     * @param rowperpage 每页显示的行数
     * @param name 商品名称
     * @param category 商品分类
     * @param min 介个区间的最小值
     * @param max 价格区间的最大值
     * @return 封装了当前页相关信息的Page对象
     */
    public Page pageList(int thispage, int rowperpage, String name, String category, double min, double max) {
            Page<Product> page=new Page();
        page.setThispage(thispage);
        page.setRowperpage(rowperpage);
        //通过查询得到符合条件的总记录数，设置总记录数
        page.setCountrow(pd.GetProdKeyConut(name,category,min,max));
        //通过总记录数初一每页记录数，得到总页数
        page.setCountpage(page.getCountrow()/rowperpage+(page.getCountrow()%rowperpage==0?0:1));
        page.setPrepage(thispage==1?thispage:thispage-1);
        page.setNextpage(thispage==page.getCountpage()?thispage:thispage+1);
        page.setList(pd.findProdsByKeyLimit((thispage-1)*rowperpage,rowperpage,name,category,min,max));
        return page;
    }

    /**
     * 通过商品id查找商品信息
     * @param id 商品id
     * @return 商品对象
     */
    public Product findProdById(String id) {
        return pd.findProdById(id);
    }

    /**
     * 通过id修改商品信息
     * @param id 商品id
     */
    public void editProdById(String id,Product prod) {
        pd.editProdById(id,prod);
    }
}
