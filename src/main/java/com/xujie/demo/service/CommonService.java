package com.xujie.demo.service;

import com.xujie.demo.model.BasePageInfo;

import java.util.List;

public interface CommonService<T> {
    BasePageInfo<T> getBasePageInfo(List<T> objectList, Integer page, Integer size);
}
