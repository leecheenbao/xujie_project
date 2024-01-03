package com.xujie.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {

    private String productId;
    private String productName;
    private BigDecimal amount;
    private Integer count;

}
