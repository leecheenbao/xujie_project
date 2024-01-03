package com.xujie.demo.controller;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.Result;
import com.xujie.demo.model.ResultGenerator;
import com.xujie.demo.model.entity.MemberEntity;
import com.xujie.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public Result getMember(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        BasePageInfo all = memberService.getAll(page, size);
        return ResultGenerator.genSuccessResult(all);
    }

    @GetMapping("/{userId}")
    public Result getMember(@PathVariable String userId) {
        MemberEntity memberEntity = memberService.getOne(userId);
        return ResultGenerator.genSuccessResult(memberEntity);
    }

    @DeleteMapping("/{userId}")
    public Result deleteMember(@PathVariable String userId) {
        memberService.delete(userId);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping
    public Result saveMember(@RequestBody MemberEntity memberEntity) {
        return ResultGenerator.genSuccessResult(memberService.save(memberEntity));
    }

    @PutMapping("/{userId}")
    public Result updateMember(@PathVariable String userId, @RequestBody MemberEntity memberEntity) {
        return ResultGenerator.genSuccessResult(memberService.update(userId, memberEntity));
    }
}
