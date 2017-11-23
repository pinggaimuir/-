package cn.tedu.dao;

import cn.tedu.domain.Product;
import cn.tedu.utils.DaoUtils;
import cn.tedu.utils.MyBeanListhandler;

import java.util.List;

/**
 * Created by tarena on 2016/9/7.
 */
public class ProdDaoImpl implements ProdDao {
    public void addProd(Product prod) {
        String sql="insert into products values(?,?,?,?,?,?,?)";
        Object[] params={prod.getId(),prod.getName(),prod.getPrice(),prod.getCategory(),prod.getPnum(),prod.getImgurl(),prod.getDescription()};
        DaoUtils.update(sql,params);
    }

    @Override
    public List<Product> listProd() {
        String sql="select * from products";
        Object[] params={};
        return DaoUtils.query(sql,new MyBeanListhandler<Product>(Product.class),params);
    }

    @Override
    public void deleteProd(String id) {
        String sql="delete from products where id=?";
        Object[] parmas={id};
        DaoUtils.update(sql,parmas);
    }
}
