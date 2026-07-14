package com.pcwk.ehr.product.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.category.domain.CategoryVO;
import com.pcwk.ehr.category.service.CategoryService;
import com.pcwk.ehr.product.domain.ProductImageVO;
import com.pcwk.ehr.product.domain.ProductSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.mapper.ProductImageMapper;
import com.pcwk.ehr.product.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LogManager.getLogger(ProductServiceImpl.class);

    private static final String STATUS_SELLING = "01";
    private static final String STATUS_RESERVED = "02";
    private static final String STATUS_SOLD = "03";
    private static final int MAX_IMAGE_COUNT = 5;
    private static final String UPLOAD_PATH = "D:\\SPAM\\Upload";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public int doInsert(ProductVO product, List<MultipartFile> files) {
        validateForSave(product);
        validateOwner(product);

        if (isEmpty(product.getStatus())) {
            product.setStatus(STATUS_SELLING);
        }
        validateStatus(product.getStatus());
        product.setAdminHideYn("N");

        int flag = productMapper.doInsert(product);
        saveImages(product.getProductNo(), files);

        LOG.debug("상품 등록 완료: productNo={}, flag={}", product.getProductNo(), flag);
        return flag;
    }

    @Override
    @Transactional
    public int doUpdate(ProductVO product, List<MultipartFile> files) {
        checkOwner(product);
        validateForSave(product);
        validateStatus(product.getStatus());

        int flag = productMapper.doUpdate(product);

        if (hasUpload(files)) {
            List<ProductImageVO> oldImages = productImageMapper.doRetrieveByProduct(product.getProductNo());
            productImageMapper.doDeleteByProduct(product.getProductNo());
            saveImages(product.getProductNo(), files);
            deletePhysicalFiles(oldImages);
        }

        LOG.debug("상품 수정 완료: productNo={}, flag={}", product.getProductNo(), flag);
        return flag;
    }

    @Override
    @Transactional
    public int doDelete(ProductVO product) {
        checkOwner(product);

        List<ProductImageVO> images = productImageMapper.doRetrieveByProduct(product.getProductNo());
        productImageMapper.doDeleteByProduct(product.getProductNo());
        int flag = productMapper.doDelete(product);

        if (flag == 1) {
            deletePhysicalFiles(images);
        }

        LOG.debug("상품 삭제 완료: productNo={}, flag={}", product.getProductNo(), flag);
        return flag;
    }

    @Override
    @Transactional
    public ProductVO doSelectOne(ProductVO product) {
        ProductVO outVO = productMapper.doSelectOne(product);

        if (outVO == null) {
            return null;
        }

        Long viewerNo = product.getUserNum();
        if (viewerNo == null || !viewerNo.equals(outVO.getUserNum())) {
            productMapper.updateViewCount(product);
            outVO.setViewCount(outVO.getViewCount() + 1);
        }

        outVO.setImageList(productImageMapper.doRetrieveByProduct(outVO.getProductNo()));
        return outVO;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVO getProduct(int productNo) {
        ProductVO param = new ProductVO();
        param.setProductNo(productNo);
        return productMapper.doSelectOne(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVO> doRetrieve(ProductSearchDTO search) {
        ProductSearchDTO safeSearch = search == null ? new ProductSearchDTO() : search;
        return productMapper.doRetrieve(safeSearch);
    }

    @Override
    @Transactional(readOnly = true)
    public int totalCnt(ProductSearchDTO search) {
        ProductSearchDTO safeSearch = search == null ? new ProductSearchDTO() : search;
        return productMapper.totalCnt(safeSearch);
    }

    @Override
    @Transactional
    public int updateStatus(ProductVO product) {
        checkOwner(product);
        validateStatus(product.getStatus());
        return productMapper.updateStatus(product);
    }

    @Override
    @Transactional
    public int plusChatCnt(ProductVO product) {
        return productMapper.plusChatCnt(product);
    }

    @Override
    @Transactional
    public int minusChatCnt(ProductVO product) {
        return productMapper.minusChatCnt(product);
    }

    private void saveImages(int productNo, List<MultipartFile> files) {
        List<MultipartFile> uploadFiles = nonEmptyFiles(files);
        if (uploadFiles.isEmpty()) {
            return;
        }
        if (uploadFiles.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("상품 이미지는 최대 5장까지 등록할 수 있습니다.");
        }

        File saveDir = new File(UPLOAD_PATH);
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            throw new IllegalStateException("상품 이미지 저장 폴더를 만들 수 없습니다.");
        }

        int order = 1;
        for (MultipartFile file : uploadFiles) {
            validateImage(file);

            String originName = file.getOriginalFilename();
            String extension = getExtension(originName);
            String changeName = UUID.randomUUID().toString().replace("-", "") + extension;

            try {
                file.transferTo(new File(saveDir, changeName));
            } catch (Exception e) {
                throw new RuntimeException("상품 이미지 저장에 실패했습니다.", e);
            }

            ProductImageVO image = new ProductImageVO();
            image.setProductNo(productNo);
            image.setOriginName(originName);
            image.setChangeName(changeName);
            image.setFilePath("/image/view.do?name=" + changeName);
            image.setImageOrder(order);
            image.setThumbnailYn(order == 1 ? "Y" : "N");
            productImageMapper.doInsert(image);
            order++;
        }
    }

    private void deletePhysicalFiles(List<ProductImageVO> images) {
        if (images == null) {
            return;
        }
        for (ProductImageVO image : images) {
            if (image == null || isEmpty(image.getChangeName())) {
                continue;
            }
            File file = new File(UPLOAD_PATH, image.getChangeName());
            if (file.exists() && !file.delete()) {
                LOG.warn("상품 이미지 파일 삭제 실패: {}", file.getAbsolutePath());
            }
        }
    }

    private List<MultipartFile> nonEmptyFiles(List<MultipartFile> files) {
        List<MultipartFile> result = new ArrayList<>();
        if (files == null) {
            return result;
        }
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                result.add(file);
            }
        }
        return result;
    }

    private boolean hasUpload(List<MultipartFile> files) {
        return !nonEmptyFiles(files).isEmpty();
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
        getExtension(file.getOriginalFilename());
    }

    private String getExtension(String fileName) {
        if (isEmpty(fileName)) {
            throw new IllegalArgumentException("이미지 파일명이 올바르지 않습니다.");
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("확장자가 있는 이미지 파일을 선택하세요.");
        }

        String extension = fileName.substring(dotIndex).toLowerCase(Locale.ROOT);
        if (!(".jpg".equals(extension) || ".jpeg".equals(extension) || ".png".equals(extension)
                || ".gif".equals(extension) || ".webp".equals(extension))) {
            throw new IllegalArgumentException("JPG, PNG, GIF, WEBP 이미지만 업로드할 수 있습니다.");
        }
        return extension;
    }

    private void validateForSave(ProductVO product) {
        if (product == null) {
            throw new IllegalArgumentException("상품 정보가 없습니다.");
        }
        if (isEmpty(product.getProductTitle())) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (product.getProductTitle().trim().length() > 100) {
            throw new IllegalArgumentException("상품명은 100자 이하로 입력하세요.");
        }
        if (isEmpty(product.getProductContent())) {
            throw new IllegalArgumentException("상품 설명은 필수입니다.");
        }
        if (product.getProductContent().length() > 1000) {
            throw new IllegalArgumentException("상품 설명은 1000자 이하로 입력하세요.");
        }
        if (product.getLocation() != null && product.getLocation().length() > 100) {
            throw new IllegalArgumentException("거래지역은 100자 이하로 입력하세요.");
        }
        if (!isEmpty(product.getProductCondition())
                && !("새상품".equals(product.getProductCondition())
                    || "사용감 적음".equals(product.getProductCondition())
                    || "사용감 있음".equals(product.getProductCondition())
                    || "수리 필요".equals(product.getProductCondition()))) {
            throw new IllegalArgumentException("허용되지 않은 상품 상태입니다.");
        }
        if (product.getCategoryNo() <= 0) {
            throw new IllegalArgumentException("카테고리를 소분류까지 선택하세요.");
        }
        CategoryVO category = categoryService.doSelectOne(product.getCategoryNo());
        if (category == null || category.getCategoryLevel() != 3) {
            throw new IllegalArgumentException("사용 가능한 소분류 카테고리를 선택하세요.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
        if (product.getPrice() > 9999999999L) {
            throw new IllegalArgumentException("가격은 9,999,999,999원 이하로 입력하세요.");
        }
    }

    private void validateOwner(ProductVO product) {
        if (product.getUserNum() == null || product.getUserNum() <= 0) {
            throw new IllegalStateException("로그인한 회원만 상품을 등록할 수 있습니다.");
        }
    }

    private void checkOwner(ProductVO product) {
        validateOwner(product);
        ProductVO dbVO = productMapper.doSelectOne(product);
        if (dbVO == null) {
            throw new IllegalStateException("존재하지 않는 상품입니다.");
        }
        if (!product.getUserNum().equals(dbVO.getUserNum())) {
            throw new IllegalStateException("본인 상품만 수정하거나 삭제할 수 있습니다.");
        }
    }

    private void validateStatus(String status) {
        if (!STATUS_SELLING.equals(status) && !STATUS_RESERVED.equals(status) && !STATUS_SOLD.equals(status)) {
            throw new IllegalArgumentException("허용되지 않은 거래상태입니다.");
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
