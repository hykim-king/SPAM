package com.pcwk.ehr.product.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.mapper.ProductMapper;
import com.pcwk.ehr.product.domain.ProductVO;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public int doInsert(ProductVO product) {
        return productMapper.doInsert(product);
    }

    @Override
    public int doUpdate(ProductVO product) {
        return productMapper.doUpdate(product);
    }

    @Override
    public int doDelete(ProductVO product) {
        return productMapper.doDelete(product);
    }

    // 상세 조회 시 조회수 증가와 데이터 조회가 한 트랜잭션 안에서 안전하게 묶이도록 처리합니다.
    @Transactional
    @Override
    public ProductVO doSelectOne(ProductVO product) {
        // 1. 조회수 1 증가
        productMapper.updateViewCount(product);
        
        // 2. 상품 상세 데이터 단건 조회 후 반환
        return productMapper.doSelectOne(product);
    }

    @Override
    public List<ProductVO> doRetrieve(ProductVO product) {
        return productMapper.doRetrieve(product);
    }
}