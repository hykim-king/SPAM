package com.pcwk.ehr.category.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.category.domain.CategoryVO;
import com.pcwk.ehr.category.mapper.CategoryMapper;

@Service
public class CategoryServiceImpl implements CategoryService {

    Logger log = LogManager.getLogger(getClass());

    @Autowired
    private CategoryMapper categoryMapper;

    /** 대분류 카테고리 목록 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveParent() {
        return categoryMapper.doRetrieveParent();
    }

    /** 부모 카테고리 번호로 하위 카테고리 목록 조회 (중분류/소분류 공용) */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveChild(int parentCategoryNo) {
        return categoryMapper.doRetrieveChild(parentCategoryNo);
	}

    /**
     * 전체 카테고리 목록 조회
     * 
     * 대/중/소분류 전체 데이터를 반환한다.
     * 메인 메뉴, 카테고리 트리, 관리자 화면 등
     * 전체 카테고리 정보가 필요한 화면에서 서버 재호출 없이 처리할 때 사용한다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> doRetrieveAll() {
        return categoryMapper.doRetrieveAll();
    }

}
