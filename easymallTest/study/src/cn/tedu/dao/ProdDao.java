package cn.tedu.dao;

import cn.tedu.domain.Product;

import java.util.List;

/**
 * Created by tarena on 2016/9/7.
 */
public interface ProdDao {
    void addProd(Product prod);
    List<Product> listProd();
    void deleteProd(String id);
}
