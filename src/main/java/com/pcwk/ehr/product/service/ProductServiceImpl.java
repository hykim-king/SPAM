package com.pcwk.ehr.product.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.product.domain.ProductImageVO;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.mapper.ProductImageMapper;
import com.pcwk.ehr.product.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

    Logger log = LogManager.getLogger(getClass());
    
    private static final String STATUS_SELLING  = "01"; // 판매중
    private static final String STATUS_RESERVED = "02"; // 예약중
    private static final String STATUS_SOLD     = "03"; // 판매완료

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private ProductImageMapper productImageMapper;   // 이미지 매퍼 주입 추가
    
    // 파일 저장 경로 (설정한 폴더)
    private static final String UPLOAD_PATH = "D:\\SPAM\\Upload";

    @Override
    @Transactional
    public int doInsert(ProductVO product, List<MultipartFile> files) {
        log.debug("1. doInsert(param): {}", product);

        // 1) 필수값/가격 검증
        validateForSave(product);

        // 2) 등록 기본값 세팅
        if (isEmpty(product.getStatus())) {
            product.setStatus(STATUS_SELLING); // 거래상태: 판매중
        }
        product.setViewCount(0); // 조회수
        product.setLikeCnt(0);   // 찜
        product.setChatCnt(0);   // 채팅수
        product.setAdminHideYn("N"); // 숨김상태

        // 3) 상품 INSERT (여기서 productNo가 생성됨)
        int flag = productMapper.doInsert(product);
        log.debug("2. 상품 등록 flag: {}, 생성된 productNo: {}", flag, product.getProductNo());

        // 4) 이미지 저장 + INSERT
        if (files != null && !files.isEmpty()) {
            int order = 1;
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;  // 빈 슬롯 건너뛰기

                try {
                    // 원본 파일명 & 새 파일명(중복방지)
                    String originName = file.getOriginalFilename();
                    String ext = originName.substring(originName.lastIndexOf("."));  // 확장자
                    String changeName = UUID.randomUUID().toString().replace("-", "") + ext;

                    // 실제 폴더에 저장
                    File saveDir = new File(UPLOAD_PATH);
                    if (!saveDir.exists()) saveDir.mkdirs();  // 폴더 없으면 생성
                    File saveFile = new File(saveDir, changeName);
                    file.transferTo(saveFile);  // ★ 디스크에 실제 저장

                    // 이미지 정보 INSERT
                    ProductImageVO img = new ProductImageVO();
                    img.setProductNo(product.getProductNo());  // 방금 생성된 상품번호
                    img.setOriginName(originName);
                    img.setChangeName(changeName);
                    img.setFilePath("/upload/" + changeName);  // 웹 접근 경로
                    img.setImageOrder(order);
                    img.setThumbnalYn(order == 1 ? "Y" : "N");  // 첫 장을 대표로
                    productImageMapper.doInsert(img);

                    order++;
                } catch (Exception e) {
                    log.error("이미지 저장 실패: {}", e.getMessage());
                    throw new RuntimeException("이미지 저장 실패", e);  // 롤백 유발
                }
            }
        }

        return flag;
    }

    @Override
    @Transactional
    public int doUpdate(ProductVO product) {
        log.debug("1. doUpdate(param): {}", product);

        checkOwner(product);        // 본인 상품만 수정
        validateForSave(product);   // 값 검증

        int flag = productMapper.doUpdate(product);
        log.debug("2. doUpdate(flag): {}", flag);
        return flag;
    }

    @Override
    @Transactional
    public int doDelete(ProductVO product) {
        log.debug("1. doDelete(param): {}", product);

        checkOwner(product);        // 본인 상품만 삭제

        int flag = productMapper.doDelete(product);
        log.debug("2. doDelete(flag): {}", flag);
        return flag;
    }

    @Override
    @Transactional
    public ProductVO doSelectOne(ProductVO product) {
        log.debug("1. doSelectOne(param): {}", product);

        ProductVO outVO = productMapper.doSelectOne(product);

        if (outVO != null) {
            // 조회수 증가
            if (product.getSallerNo() != outVO.getSallerNo()) {
                productMapper.updateViewCount(product);
                outVO.setViewCount(outVO.getViewCount() + 1);
            }

            // 이미지 목록 조회해서 담기
            List<ProductImageVO> imageList =
                productImageMapper.doRetrieveByProduct(outVO.getProductNo());
            outVO.setImageList(imageList);
            log.debug("이미지 {}장 조회됨", imageList.size());
        }

        log.debug("2. doSelectOne(outVO): {}", outVO);
        return outVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVO> doRetrieve() {
    	
        List<ProductVO> list = productMapper.doRetrieve();
        return list;
    }
    
	@Override
	@Transactional
	public int updateStatus(ProductVO product) {
	    log.debug("1. updateStatus(param): {}", product);

	    checkOwner(product); // 본인 상품만 상태 변경 가능

	    String status = product.getStatus();
	    
	    if (!STATUS_SELLING.equals(status)
	            && !STATUS_RESERVED.equals(status)
	            && !STATUS_SOLD.equals(status)) {
	        throw new IllegalArgumentException("허용되지 않은 거래상태: " + status);
	    }

	    int flag = productMapper.updateStatus(product);
	    log.debug("2. updateStatus(flag): {}", flag);
	    
	    return flag;
	}

	@Override
	public int plusChatCnt(ProductVO product) {
		return productMapper.plusChatCnt(product);
	}

	@Override
	public int minusChatCnt(ProductVO product) {
		return productMapper.minusChatCnt(product);
	}
    
    // ───────────────────── 내부 로직 ─────────────────────

    /** 등록/수정 공통 검증 */
    private void validateForSave(ProductVO product) {
        if (product == null) {
            throw new IllegalArgumentException("상품 정보가 없습니다.");
        }
        if (isEmpty(product.getProductTitle())) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (product.getCategoryNo() <= 0) {
            throw new IllegalArgumentException("카테고리를 선택하세요.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    /** 로그인 회원이 해당 상품의 판매자인지 확인 */
    private void checkOwner(ProductVO product) {
        ProductVO dbVO = productMapper.doSelectOne(product);
        if (dbVO == null) {
            throw new IllegalStateException("존재하지 않는 상품입니다.");
        }
        if (dbVO.getSallerNo() != product.getSallerNo()) {
            throw new IllegalStateException("본인 상품만 수정/삭제할 수 있습니다.");
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}