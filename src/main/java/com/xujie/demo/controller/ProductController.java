package com.xujie.demo.controller;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.Result;
import com.xujie.demo.model.ResultGenerator;
import com.xujie.demo.model.entity.ProductEntity;
import com.xujie.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result getProduct(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        BasePageInfo all = productService.getAll(page, size);
        return ResultGenerator.genSuccessResult(all);
    }

    @GetMapping("/{productId}")
    public Result getProduct(@PathVariable String productId) {
        ProductEntity productEntity = productService.getOne(productId);
        return ResultGenerator.genSuccessResult(productEntity);
    }

    @DeleteMapping("/{productId}")
    public Result deleteProduct(@PathVariable String productId) {
        productService.delete(productId);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping
    public Result saveProduct(@RequestBody ProductEntity productEntity) {
        return ResultGenerator.genSuccessResult(productService.save(productEntity));
    }

    @PutMapping("/{productId}")
    public Result updateProduct(@PathVariable String productId, @RequestBody ProductEntity productEntity) {
        productService.update(productId, productEntity);
        return ResultGenerator.genSuccessResult();
    }
}
