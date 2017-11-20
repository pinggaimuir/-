package cn.tedu.service;

import cn.tedu.domain.Product;
import cn.tedu.utils.Page;

import java.util.List;

/**
 * Created by tarena on 2016/9/6.
 */
public interface ProdService {
    /**
     * 商品添加方法
     * @param prod 商品对象
     */
    void addProduct(Product prod);

    /**
     * 查询商品列表
     * @return
     */
    List<Product> prodList();

    /**
     * 根据商品Id删除商品
     * @param id 商品id
     * @return 删除的数量
     */
    int deleteProd(String id);

    /**
     * 根据商品id更改商品的数量
     * @param id 商品id
     * @param pnum 更改的新数量
     */
    void updatePnum(String id, int pnum);

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
    Page pageList(int thispage, int rowperpage, String name, String category, double min, double max);

    /**
     * 通过商品id查找商品
     * @param id
     * @return
     */
    Product findProdById(String id);

    /**
     * 通过id修改商品信息
     * @param id 商品id
     */
    void editProdById(String id,Product prod);
}
