package com.pcwk.ehr.category.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.category.domain.CategoryVO;
import com.pcwk.ehr.category.mapper.CategoryMapper;

public class CategoryServiceImpl implements CategoryService {

    Logger log = LogManager.getLogger(getClass());

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveParent() {
        return categoryMapper.doRetrieveParent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveChild(int parentCategoryNo) {
        return categoryMapper.doRetrieveChild(parentCategoryNo);
	}

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveAll() {
        return categoryMapper.doRetrieveAll();
    }

}
