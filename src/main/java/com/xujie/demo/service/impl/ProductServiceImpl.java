package com.xujie.demo.service.impl;

import com.xujie.demo.exception.BusinessException;
import com.xujie.demo.exception.CommonError;
import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.entity.ProductEntity;
import com.xujie.demo.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends CommonServiceImpl implements ProductService {

    private List<ProductEntity> productEntityList = new ArrayList<>();

    // 在初始化時建立初始資料
    @PostConstruct
    public void init() {
        save(ProductEntity.builder().productId(String.valueOf(UUID.randomUUID())).productName("pen").count(10).amount(BigDecimal.valueOf(10)).build());
        save(ProductEntity.builder().productId(String.valueOf(UUID.randomUUID())).productName("pencil").count(10).amount(BigDecimal.valueOf(50)).build());
        save(ProductEntity.builder().productId(String.valueOf(UUID.randomUUID())).productName("ruler").count(10).amount(BigDecimal.valueOf(30)).build());

    }

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        String productId = String.valueOf(UUID.randomUUID());
        productEntity.setProductId(productId);
        // 檢查是否有相同 ID 的成員已存在
        if (productEntityList.stream().anyMatch(m -> m.getProductId().equals(productEntity.getProductId()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        // 檢查是否有相同名字的成員已存在
        if (productEntityList.stream().anyMatch(m -> m.getProductName().equals(productEntity.getProductName()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        productEntityList.add(productEntity);

        return productEntity;
    }

    @Override
    public void delete(String productId) {
        productEntityList.remove(getOne(productId));
    }

    @Override
    public ProductEntity update(String productId, ProductEntity productEntity) {
        // 找到要更新的成員
        ProductEntity existingProductEntity = getOne(productId);

        if (existingProductEntity != null) {
            // 更新成員的屬性
            existingProductEntity.setProductName(productEntity.getProductName());
            existingProductEntity.setAmount(productEntity.getAmount());
            existingProductEntity.setCount(productEntity.getCount());
        } else {
            // 如果成員不存在，您可能希望拋出一個適當的例外或進行其他處理
            throw new BusinessException(CommonError.DATA_NOT_EXISTED);
        }

        return existingProductEntity;
    }

    public ProductEntity getOne(String productId) {
        ProductEntity productEntity = productEntityList.stream()
                .filter(m -> m.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (productEntity == null) {
            throw new BusinessException(CommonError.PRODUCT_NOT_EXISTED);
        }

        return productEntity;
    }


    @Override
    public BasePageInfo getAll(Integer page, Integer size) {
        return getBasePageInfo(productEntityList, page, size);
    }

    @Override
    public ProductEntity reduce(String productId, Integer count) {
        ProductEntity product = getOne(productId);
        int effectiveCount = product.getCount() - count;

        if (effectiveCount < 0){
            throw new BusinessException(CommonError.PRODUCT_NOT_ENOUGH);
        }

        product.setCount(effectiveCount);
        return update(productId, product);
    }

}
