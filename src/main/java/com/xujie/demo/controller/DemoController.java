package com.xujie.demo.controller;

import com.xujie.demo.model.Result;
import com.xujie.demo.model.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class DemoController {

    @GetMapping
    public Result helloWorld() {
        return ResultGenerator.genSuccessResult("Hello World !!!!!");
    }
}
