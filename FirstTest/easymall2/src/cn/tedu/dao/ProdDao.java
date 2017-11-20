package cn.tedu.dao;

import cn.tedu.domain.Product;

import java.util.List;

/**
 * Created by tarena on 2016/9/6.
 */
public interface ProdDao {
    /**
     * 添加商品的方法
     * @param prod 商品对象
     */
    void addProduct(Product prod);

    /**
     * 后太显示商品的方法
     * @return
     */
    List<Product> prodList();

    /**
     * 根据id删除商品
     * @param id
     */
    int deleteProd(String id);

    /**
     * 根据商品id跟新商品库存
     * @param id 商品的id
     * @param pnum 跟新后的数量
     */
    void updatePnum(String id, int pnum);
    /**
     * 分页查询之查询符合条件的商品的总数量
     * @param name 商品名称
     * @param category 商品分类
     * @param min 价格区间的最小值
     * @param max 价格区间的最大值
     * @return 符合条件的商品的总数量
     */
    int GetProdKeyConut(String name,String category,double min,double max);

    /**
     * 分页查询之：查询符合条件的商品集合
     * @param start 从哪一个开始查询
     * @param rowperpage 查询到少条
     * @param name 商品名
     * @param category 商品分类
     * @param min 价格区间的最小值
     * @param max 价格区间的最大值
     * @return 返回符合条件的商品的集合
     */
    List<Product> findProdsByKeyLimit(int start,int rowperpage,String name,String category,double min,double max);

    /**
     * 通过商品id查询商品信息
     * @param id 商品id
     * @return 商品对象
     */
    Product findProdById(String id);
    /**
     * 通过id修改商品信息
     * @param id 商品id
     */
    void editProdById(String id,Product prod);
    /*事物版*/
    Product findProdByIdtx(String id);
    void editProdByIdtx(String id,Product prod);

}
