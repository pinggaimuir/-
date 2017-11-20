package cn.tedu.dao;

import cn.tedu.domain.Product;
import cn.tedu.utils.DaoUtils;

import java.util.List;

/**
 * Created by tarena on 2016/9/6.
 */
public class ProdDaoImpl implements ProdDao {
    /**
     * 添加商品的方法
     * @param prod 商品对象
     */
    public void addProduct(Product prod) {
        String sql="insert into products(id,name,price,category,pnum,imgurl,description) values(?,?,?,?,?,?,?)";
        Object[] params={prod.getId(),prod.getName(),prod.getPrice(),prod.getCategory(),prod.getPnum(),prod.getImgurl(),prod.getDescription()};
        DaoUtils.update(sql,params);
    }

    /**
     * 显示商品列表的方法
     * @return
     */
    public List<Product> prodList() {
        String sql="select * from products";
        Object[] params={};
        return DaoUtils.queryList(sql,Product.class,params);
    }

    /**
     * 根据id删除商品
     * @param id
     */
    public int deleteProd(String id) {

        String sql="delete from products where id=?";
        Object[] parmas={id};
        return (int)DaoUtils.update(sql,parmas);
    }

    /**
     * 根据商品id跟新商品数量
     * @param id 商品的id
     * @param pnum 跟新后的数量
     */
    public void updatePnum(String id, int pnum) {
        String sql="update products set pnum=? where id=?";
        Object[] params={pnum,id};
        DaoUtils.update(sql,params);
    }

    /**
     * 分页查询之查询符合条件的商品的总数量
     * @param name 商品名称
     * @param category 商品分类
     * @param min 价格区间的最小值
     * @param max 价格区间的最大值
     * @return 符合条件的商品的总数量
     */
    public int GetProdKeyConut(String name, String category, double min, double max) {
        String sql="select count(*) from products where name like ? and category like ? and price>? and price<?";
        Object[] params={"%"+name+"%","%"+category+"%",min,max};
        return DaoUtils.queryCount(sql,params);
    }

    /**
     * 查询所有符合条件的商品的的集合
     * @param start 从哪一个开始查询
     * @param rowperpage 查询到少条
     * @param name 商品名
     * @param category 商品分类
     * @param min 价格区间的最小值
     * @param max 价格区间的最大值
     * @return 符合条件的商品的集合
     */
    public List<Product> findProdsByKeyLimit(int start, int rowperpage, String name, String category, double min, double max) {
        String sql="select * from products where name like ? and category like ? and price>? and price<? limit ?,?";
        Object[] params={"%"+name+"%","%"+category+"%",min,max,start,rowperpage};
        return DaoUtils.queryList(sql,Product.class,params);
    }

    /**
     * 通过商品id查询商品信息
     * @param id 商品id
     * @return 商品对象
     */
    public Product findProdById(String id) {
        String sql="select * from products where id=?";
        Object[] params={id};
         return DaoUtils.query(sql,Product.class,params);
    }

    /**
     * 通过id修改商品信息
     * @param id 商品id
     */
    public void editProdById(String id,Product prod) {
        String sql="update products set name=?,price=?,category=?,pnum=?,imgurl=?,description=? where id=?";
        Object[] params={prod.getName(),prod.getPrice(),prod.getCategory(),prod.getPnum(),prod.getImgurl(),prod.getDescription(),prod.getId()};
        DaoUtils.update(sql,params);
    }

    /**
     * 通过商品id查询商品信息,需要自己提供数据库链接
     * @param id 商品id
     * @return 商品对象
     */
    public Product findProdByIdtx(String id) {
        String sql="select * from products where id=?";
        Object[] params={id};
        return DaoUtils.txquery(sql,Product.class,params);
    }

    /**
     * 通过id修改商品信息
     * @param id 商品id

     */
    public void editProdByIdtx(String id,Product prod) {
        String sql="update products set name=?,price=?,category=?,pnum=?,imgurl=?,description=? where id=?";
        Object[] params={prod.getName(),prod.getPrice(),prod.getCategory(),prod.getPnum(),prod.getImgurl(),prod.getDescription(),prod.getId()};
        DaoUtils.txupdate(sql,params);
    }

}
