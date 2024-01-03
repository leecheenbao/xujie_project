package com.xujie.demo.service;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.entity.MemberEntity;

public interface MemberService {

    MemberEntity save(MemberEntity entity);

    void delete(String id);

    MemberEntity update(String id, MemberEntity entity);

    MemberEntity getOne(String id);

    BasePageInfo getAll(Integer page, Integer size);

}
