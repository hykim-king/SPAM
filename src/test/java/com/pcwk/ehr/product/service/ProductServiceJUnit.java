package com.pcwk.ehr.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.mapper.ProductMapper;
import com.pcwk.ehr.product.domain.ProductVO;

// 1. 대장 프로젝트 맞춤형 스프링 테스트 확장 설정
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class ProductServiceJUnit {

    // 대장 프로젝트 표준 로거(Log4j2) 적용
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ProductMapper productMapper; // productMapper.xml과 연결된 인터페이스

    // ⭐ [중요] 테스트를 수행할 실제 DB에 존재하는 회원 번호를 적어주세요!
    private final int EXISTING_SALLER_NO = 1; 
    
    // 테스트에 사용할 고정 데이터 객체
    private ProductVO product01;

    @BeforeEach
    public void setUp() throws Exception {
        log.debug("========================================");
        log.debug("= setUp() : 테스트 전 샘플 데이터 세팅 =");
        log.debug("========================================");
        
        // 대장이 올려준 productMapper.xml 구조에 맞춰 픽스처(Fixture) 생성
        product01 = new ProductVO();
        product01.setSallerNo(EXISTING_SALLER_NO); // 외래키 제한 통과용 실제 유저 번호
        product01.setCategoryNo(10);             // 카테고리 번호 (대장 DB에 있는 값으로 조정 가능)
        product01.setProductTitle("JUnit 테스트 상품 제목");
        product01.setProductContent("이것은 JUnit5를 통해 자동으로 등록된 상품 상세 내용입니다.");
        product01.setProductCondition("01");    // 상품 상태
        product01.setPrice(45000);               // 가격
        product01.setLocation("서울시 마포구");
    }

    @Test
    public void doSaveAndToOne() throws Exception {
        log.debug("========================================");
        log.debug("= test: 상품 등록 및 단건 조회 통합 테스트 =");
        log.debug("========================================");

        // STEP 1. 기존에 남아있을지 모르는 테스트 상품은 가볍게 넘어가거나 삭제 (있다면)
        // (대장의 xml에 doDelete가 있다면 실행하도록 구성 가능, 여기선 바로 Insert 테스트)

        // STEP 2. 상품 등록 (doInsert) 수행
        log.debug("1. 상품 등록 시작: " + product01);
        int flag = productMapper.doInsert(product01);
        
        // 대장 스타일의 단언문(Assertion) 검증: 1행이 성공적으로 들어갔는지 확인
        assertEquals(1, flag, "상품 등록에 실패했습니다.");
        log.debug("1. 상품 등록 성공 여부(flag): " + flag);

        // STEP 3. 전체 목록을 조회해서 방금 넣은 상품의 시퀀스 번호(PRODUCT_NO) 가져오기
        // (MyBatis가 자동으로 키를 돌려주지 않는 구조일 때 안전하게 검증하는 가장 정석적인 방법)
        List<ProductVO> list = productMapper.doRetrieve();
        assertNotNull(list, "상품 목록 리스트가 null입니다.");
        assertEquals(true, list.size() > 0, "조회된 상품 목록이 없습니다.");
        log.debug("list: "+ list);
        
        // 최근에 등록된 첫 번째 상품의 번호를 획득
        int latestProductNo = list.get(0).getProductNo();
        log.debug("2. 방금 등록된 상품 번호(시퀀스): " + latestProductNo);

        // STEP 4. 방금 넣은 번호로 상세 조회(doSelectOne) 수행해서 값이 똑같은지 검증
        ProductVO searchVO = new ProductVO();
        searchVO.setProductNo(latestProductNo); // 받아온 시퀀스 번호를 세팅

        ProductVO savedProduct = productMapper.doSelectOne(searchVO); // 객체로 전달!
        assertNotNull(savedProduct, "상세 조회된 상품 객체가 null입니다.");
        log.debug("3. 상세 조회 완료: " + savedProduct);

        // STEP 5. 입력한 데이터와 DB에서 꺼내온 데이터가 완벽히 일치하는지 최종 확인
        assertEquals(product01.getProductTitle(), savedProduct.getProductTitle(), "상품 제목이 일치하지 않습니다.");
        assertEquals(product01.getPrice(), savedProduct.getPrice(), "상품 가격이 일치하지 않습니다.");
        assertEquals("01", savedProduct.getStatus(), "기본 거래 상태가 'SALE'이 아닙니다.");
        assertEquals("N", savedProduct.getAdminHideYn(), "기본 관리자 숨김 여부가 'N'이 아닙니다.");
    }
    
    @Test
    public void beanChainCheck() {
        // 스프링 컨테이너와 매퍼 빈이 제대로 주입되었는지 껍데기 체크
        assertNotNull(context);
        assertNotNull(productMapper);
        log.debug("----------------------------------------");
        log.debug("- 주입성공 주소 확인 -");
        log.debug("ApplicationContext: " + context);
        log.debug("ProductMapper: " + productMapper);
        log.debug("----------------------------------------");
    }
}