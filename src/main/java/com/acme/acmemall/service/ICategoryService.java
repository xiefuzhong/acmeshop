package com.acme.acmemall.service;


import com.acme.acmemall.model.CategoryVo;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    List<CategoryVo> queryCategoryList(Map<String, Object> map);
}
