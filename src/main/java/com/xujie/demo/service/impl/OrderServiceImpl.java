package com.xujie.demo.service.impl;

import com.xujie.demo.exception.BusinessException;
import com.xujie.demo.exception.CommonError;
import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.dto.OrderForm;
import com.xujie.demo.model.dto.OrderQuery;
import com.xujie.demo.model.entity.MemberEntity;
import com.xujie.demo.model.entity.OrderEntity;
import com.xujie.demo.model.entity.ProductEntity;
import com.xujie.demo.service.MemberService;
import com.xujie.demo.service.OrderService;
import com.xujie.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends CommonServiceImpl implements OrderService {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;

    private List<OrderEntity> orderEntityList = new ArrayList<>();
    @PostConstruct
    public void init() {
        List<MemberEntity> memberList = (List<MemberEntity>) memberService.getAll(1, Integer.MAX_VALUE).getList();
        List<ProductEntity> productList = (List<ProductEntity>) productService.getAll(1, Integer.MAX_VALUE).getList();
        System.out.println(memberList);
        System.out.println(memberList.get(1));

        OrderForm orderForm_1 = new OrderForm();
        orderForm_1.setUserId(memberList.get(1).getUserId());
        orderForm_1.setProductId(productList.get(1).getProductId());
        orderForm_1.setCount(2);
        addOrder(orderForm_1);
        addOrder(orderForm_1);

        OrderForm orderForm_2 = new OrderForm();
        orderForm_2.setUserId(memberList.get(2).getUserId());
        orderForm_2.setProductId(productList.get(1).getProductId());
        orderForm_2.setCount(3);
        addOrder(orderForm_2);
    }
    @Override
    public OrderEntity addOrder(OrderForm orderForm) {

        MemberEntity member = memberService.getOne(orderForm.getUserId());
        ProductEntity product = productService.getOne(orderForm.getProductId());

        // 計算商品數量及價格
        BigDecimal amountTotal = product.getAmount().multiply(BigDecimal.valueOf(orderForm.getCount()));
        String orderId = String.valueOf(UUID.randomUUID());

        // 判斷商品是否足夠
        if (isNotEnough(product.getCount(), orderForm.getCount())){
            throw new BusinessException(CommonError.PRODUCT_NOT_ENOUGH);
        }

        // 減去庫存
        productService.reduce(product.getProductId(), orderForm.getCount());

        OrderEntity orderEntity = OrderEntity.builder()
                .orderId(orderId)
                .userId(member.getUserId())
                .username(member.getUsername())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .amountTotal(amountTotal)
                .count(orderForm.getCount())
                .orderDate(ZonedDateTime.now())
                .build();

        // 檢查是否有相同 ID 的訂單已存在
        if (orderEntityList.stream().anyMatch(m -> m.getOrderId().equals(orderEntity.getOrderId()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        // 建立訂單
        orderEntityList.add(orderEntity);

        return orderEntity;
    }

    public Boolean isNotEnough(Integer effectiveCount, Integer count) {
        // 判斷有效存量
        return effectiveCount - count < 0;
    }

    @Override
    public BasePageInfo getOrders(Integer page, Integer size) {
        return getBasePageInfo(orderEntityList, page, size);
    }

    @Override
    public BasePageInfo getOrdersByCriteria(OrderQuery criteria, Integer page, Integer size) {
        List<OrderEntity> filteredOrders = orderEntityList.stream()
                .filter(order -> matchesCriteria(order, criteria))
                .collect(Collectors.toList());

        return getBasePageInfo(filteredOrders, page, size);
    }

    public List<OrderEntity> getOrdersByUserId(String userId) {
        return orderEntityList.stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public BasePageInfo getOrderStatistics(Integer count, Integer page, Integer size) {
        List<MemberEntity> membersWithHighAmountOrders = (List<MemberEntity>) memberService.getAll(1, Integer.MAX_VALUE).getList().stream()
                .filter(member -> hasOrdersExceedingAmount((MemberEntity) member, count))
                .collect(Collectors.toList());

        return getBasePageInfo(membersWithHighAmountOrders, page, size);
    }

    private boolean hasOrdersExceedingAmount(MemberEntity member, Integer count) {
        List<OrderEntity> ordersByUserId = getOrdersByUserId(member.getUserId());
        BigDecimal totalOrderAmount = ordersByUserId.stream()
                .map(order -> new BigDecimal(order.getCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalOrderAmount.compareTo(new BigDecimal(count)) > 0;
    }

    private boolean matchesCriteria(OrderEntity order, OrderQuery criteria) {
        if (criteria.getOrderId() != null && !order.getOrderId().equals(criteria.getOrderId())) {
            return false;
        }

        if (criteria.getProductName() != null && !order.getProductName().equals(criteria.getProductName())) {
            return false;
        }

        if (criteria.getOrderDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            // 將前端傳來的日期字串轉換為 ZonedDateTime
            ZonedDateTime criteriaDate = ZonedDateTime.parse(criteria.getOrderDate() + "T00:00:00Z", formatter);

            // 判斷日期是否相等
            if (!order.getOrderDate().toLocalDate().isEqual(criteriaDate.toLocalDate())) {
                return false;
            }
        }

        return true;
    }

}
