package com.pcwk.ehr.category.service;

import java.util.List;

import com.pcwk.ehr.category.domain.CategoryVO;

public interface CategoryService {
    /** 대분류 목록 */
    List<CategoryVO> doRetrieveParent();

    /** 자식 카테고리 목록 (부모번호로) */
    List<CategoryVO> doRetrieveChild(int parentCategoryNo);
    
    /** 전체 카테고리 목록 */
    List<CategoryVO> doRetrieveAll();
}
