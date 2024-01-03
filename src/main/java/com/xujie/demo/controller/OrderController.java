package com.xujie.demo.controller;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.Result;
import com.xujie.demo.model.ResultGenerator;
import com.xujie.demo.model.dto.OrderForm;
import com.xujie.demo.model.dto.OrderQuery;
import com.xujie.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        BasePageInfo all = orderService.getOrders(page, size);
        return ResultGenerator.genSuccessResult(all);
    }

    /**
     * 根據 訂單編號(orderId)、產品名稱(productName)、購買日期(orderDate)
     **/
    @GetMapping("/orders")
    public Result getOrders(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String orderDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        OrderQuery orderQuery = OrderQuery.builder()
                .orderId(orderId)
                .productName(productName)
                .orderDate(orderDate)
                .build();

        return ResultGenerator.genSuccessResult(orderService.getOrdersByCriteria(orderQuery, page, size));
    }

    @PostMapping
    public Result saveProduct(@RequestBody OrderForm orderForm) {
        return ResultGenerator.genSuccessResult(orderService.addOrder(orderForm));
    }

    @GetMapping("/order-statistics")
    public Result getOrderStatistics(
            @RequestParam Integer count,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return ResultGenerator.genSuccessResult(orderService.getOrderStatistics(count, page, size));
    }

}
