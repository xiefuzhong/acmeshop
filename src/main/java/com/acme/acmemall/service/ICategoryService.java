package com.acme.acmemall.service;


import com.acme.acmemall.model.CategoryVo;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    /**
     * 列表查询
     * @param map
     * @return
     */
    List<CategoryVo> queryCategoryList(Map<String, Object> map);

    /**
     * 详情查询
     * @param id
     * @return
     */
    CategoryVo queryObject(Integer id);
}
