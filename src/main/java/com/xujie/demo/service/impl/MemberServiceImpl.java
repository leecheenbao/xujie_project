package com.xujie.demo.service.impl;

import com.xujie.demo.exception.BusinessException;
import com.xujie.demo.exception.CommonError;
import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.model.entity.MemberEntity;
import com.xujie.demo.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberServiceImpl extends CommonServiceImpl implements MemberService {

    private List<MemberEntity> memberEntityList = new ArrayList<>();

    // 在初始化時建立初始資料
    @PostConstruct
    public void init() {
        save(MemberEntity.builder().userId(String.valueOf(UUID.randomUUID())).username("John").email("john@example.com").build());
        save(MemberEntity.builder().userId(String.valueOf(UUID.randomUUID())).username("Alice").email("alice@example.com").build());
        save(MemberEntity.builder().userId(String.valueOf(UUID.randomUUID())).username("Bob").email("bob@example.com").build());
    }

    @Override
    public MemberEntity save(MemberEntity memberEntity) {
        String userId = String.valueOf(UUID.randomUUID());
        memberEntity.setUserId(userId);
        // 檢查是否有相同 ID 的成員已存在
        if (memberEntityList.stream().anyMatch(m -> m.getUserId().equals(memberEntity.getUserId()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        // 檢查是否有相同名字的成員已存在
        if (memberEntityList.stream().anyMatch(m -> m.getUsername().equals(memberEntity.getUsername()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        // 檢查是否有相同 email 的成員已存在
        if (memberEntityList.stream().anyMatch(m -> m.getEmail().equals(memberEntity.getEmail()))) {
            throw new BusinessException(CommonError.DATA_EXISTED);
        }

        memberEntityList.add(memberEntity);

        return memberEntity;
    }

    @Override
    public void delete(String userId) {
        memberEntityList.remove(getOne(userId));
    }

    @Override
    public MemberEntity update(String userId, MemberEntity memberEntity) {
        MemberEntity existingMemberEntity = getOne(userId);

        if (existingMemberEntity != null) {
            existingMemberEntity.setEmail(memberEntity.getEmail());
            existingMemberEntity.setUsername(memberEntity.getUsername());
        } else {
            throw new BusinessException(CommonError.DATA_NOT_EXISTED);
        }

        return existingMemberEntity;
    }

    @Override
    public MemberEntity getOne(String id) {
        MemberEntity member = memberEntityList.stream()
                .filter(m -> m.getUserId().equals(id))
                .findFirst()
                .orElse(null);

        if (member == null) {
            throw new BusinessException(CommonError.MEMBER_NOT_EXISTED);
        }

        return member;
    }

    @Override
    public BasePageInfo getAll(Integer page, Integer size) {
        return getBasePageInfo(memberEntityList, page, size);
    }

}
