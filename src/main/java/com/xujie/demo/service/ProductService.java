package com.xujie.demo.service;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.entity.ProductEntity;

public interface ProductService {

    ProductEntity save(ProductEntity entity);

    void delete(String productId);

    ProductEntity update(String productId, ProductEntity entity);

    ProductEntity getOne(String productId);

    BasePageInfo getAll(Integer page, Integer size);

    ProductEntity reduce(String productId, Integer count);


}
