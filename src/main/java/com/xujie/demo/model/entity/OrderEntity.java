package com.xujie.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

    private String orderId;

    private String userId;
    private String username;

    private String productId;
    private String productName;

    private BigDecimal amountTotal;
    private Integer count;

    private ZonedDateTime orderDate;
}
