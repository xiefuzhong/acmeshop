package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.CategoryMapper;
import com.acme.acmemall.model.CategoryVo;
import com.acme.acmemall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryMapper categoryDao;
    /**
     * @param map
     * @return
     */
    @Override
    public List<CategoryVo> queryCategoryList(Map<String, Object> map) {
        return categoryDao.queryList(map);
    }
}
