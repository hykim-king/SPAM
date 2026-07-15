package com.pcwk.ehr.product.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 업로드된 상품 이미지 서빙 컨트롤러.
 * web.xml의 url-pattern이 *.do 이므로, 이미지도 .do 요청으로 받아
 * 디스크(UPLOAD_PATH)에서 읽어 응답한다.
 */
@Controller
public class ProductImageController {

    private static final Logger LOG = LogManager.getLogger(ProductImageController.class);

    // 이미지 저장 폴더 (ProductServiceImpl.UPLOAD_PATH 와 동일해야 함)
    private static final String UPLOAD_PATH = "D:\\SPAM\\Upload";

    /** 이미지 서빙: /image/view.do?name=xxxx.png */
    @GetMapping("/image/view.do")
    public void viewImage(@RequestParam("name") String name,
                          HttpServletResponse response) throws IOException {

        // 경로 탈출 공격 차단 (../, 슬래시 포함 시 거부)
        if (name == null || name.contains("..") || name.contains("/") || name.contains("\\")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File file = new File(UPLOAD_PATH, name);

        // 파일 없거나 디렉터리면 404
        if (!file.exists() || !file.isFile()) {
            LOG.debug("이미지 없음: {}", file.getAbsolutePath());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 확장자로 콘텐츠 타입 결정 (image/png, image/jpeg ...)
        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            response.setContentType(contentType);
        }
        response.setContentLengthLong(file.length());

        // 파일 내용을 응답 스트림으로 전송
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.transferTo(response.getOutputStream());
        } catch (IOException e) {
            LOG.warn("이미지 전송 실패: {}", file.getAbsolutePath(), e);
            throw e;
        }
    }
}