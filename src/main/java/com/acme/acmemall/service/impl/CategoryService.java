package com.acme.acmemall.service.impl;

import com.acme.acmemall.model.CategoryVo;
import com.acme.acmemall.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService implements ICategoryService {
    /**
     * @param map
     * @return
     */
    @Override
    public List<CategoryVo> queryCategoryList(Map<String, Object> map) {
        return null;
    }
}
