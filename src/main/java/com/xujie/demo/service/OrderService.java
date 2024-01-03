package com.xujie.demo.service;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.dto.OrderForm;
import com.xujie.demo.model.dto.OrderQuery;
import com.xujie.demo.model.entity.OrderEntity;

public interface OrderService {

    OrderEntity addOrder(OrderForm orderForm);

    BasePageInfo getOrders(Integer page, Integer size);

    BasePageInfo getOrdersByCriteria(OrderQuery criteria, Integer page, Integer size);

    BasePageInfo getOrderStatistics(Integer count, Integer page, Integer size);

}
