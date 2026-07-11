package com.pcwk.ehr.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.category.domain.CategoryVO;

@Mapper
public interface CategoryMapper {
    /** 대분류 목록 조회 */
    List<CategoryVO> doRetrieveParent();

    /** 자식 카테고리 조회 (대분류→중분류, 중분류→소분류 공용) */
    List<CategoryVO> doRetrieveChild(@Param("parentCategoryNo") int parentCategoryNo);
    
    /** 전체 카테고리 조회 (대+중+소 한번에) */
    List<CategoryVO> doRetrieveAll();

    /** 사용 중인 카테고리 단건 조회 */
    CategoryVO doSelectOne(@Param("categoryNo") int categoryNo);
}
