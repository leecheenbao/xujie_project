package com.xujie.demo.service.impl;

import com.xujie.demo.model.BasePageInfo;
import com.xujie.demo.service.CommonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public BasePageInfo getBasePageInfo(List objectList, Integer page, Integer size) {
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, objectList.size());

        List pageList = objectList.subList(fromIndex, toIndex);

        BasePageInfo basePageInfo = new BasePageInfo<>();
        basePageInfo.setList(pageList);
        basePageInfo.setTotal(objectList.size());

        return basePageInfo;
    }
}
