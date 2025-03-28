package com.mbc.day05.mapper;

import com.mbc.day05.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getAllProducts();
    Product getProductById(Long productId);
    void insertProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Long productId);
}

