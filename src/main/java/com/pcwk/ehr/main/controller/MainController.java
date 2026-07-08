package com.pcwk.ehr.main.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 화면 Controller
 *
 * [추가] 현재 상품관리 모듈이 완전히 연동되기 전까지는 메인 화면 확인용 더미 상품 데이터를 사용한다.
 * [추가] 추후 ProductService가 합쳐지면 sampleProductList() 대신 Service 조회 결과를 Model에 담으면 된다.
 */
@Controller
public class MainController {

    /**
     * 메인 화면 이동
     *
     * 요청 URL:
     * - /main.do
     * - /index.do
     *
     * 반환 View:
     * - /WEB-INF/views/index.jsp
     */
    @GetMapping({ "/main.do", "/index.do" })
    public String index(Model model) {
        List<Map<String, Object>> products = sampleProductList();

        // [수정 필요] 상품관리 모듈 연동 후 실제 인기 상품 조회 결과로 교체
        model.addAttribute("popularProductList", products.subList(0, 4));

        // [수정 필요] 상품관리 모듈 연동 후 실제 추천 상품 조회 결과로 교체
        model.addAttribute("recommendedProductList", products.subList(4, 8));

        // [수정 필요] 상품관리 모듈 연동 후 실제 최신 등록 상품 조회 결과로 교체
        model.addAttribute("latestProductList", products.subList(8, 12));

        return "index";
    }

    /**
     * [임시] 메인 화면 UI 확인용 상품 데이터 15개 중 화면에는 섹션별 최대 4개만 노출
     *
     * [수정 필요] 추후 아래 기준으로 DB 조회를 붙이면 된다.
     * - 인기 상품 Top: 조회수 또는 찜 수 기준
     * - 오늘의 추천: 관리자 추천 여부 또는 추천 알고리즘 기준
     * - 최신 등록 상품: 등록일 최신순 기준
     */
    private List<Map<String, Object>> sampleProductList() {
        List<Map<String, Object>> list = new ArrayList<>();

        list.add(product(1, "아이패드 프로 11인치 4세대", 650000, "서울 마포구", "30분 전", "p01_tablet.svg", 128));
        list.add(product(2, "나이키 덩크 로우 블랙", 120000, "서울 강남구", "1시간 전", "p02_shoes.svg", 96));
        list.add(product(3, "마르지엘라 5AC 미니백", 480000, "서울 서초구", "2시간 전", "p03_bag.svg", 42));
        list.add(product(4, "스탠리 퀜처 텀블러 1.18L", 45000, "서울 용산구", "3시간 전", "p04_tumbler.svg", 23));
        list.add(product(5, "다이슨 슈퍼소닉 드라이어", 220000, "경기 성남시", "4시간 전", "p05_dryer.svg", 77));

        list.add(product(6, "원목 미니 3단 선반", 45000, "서울 은평구", "오늘", "p06_shelf.svg", 31));
        list.add(product(7, "갤럭시 버즈2 프로 이어폰", 90000, "인천 남동구", "오늘", "p07_earbuds.svg", 18));
        list.add(product(8, "접이식 캠핑 의자", 18000, "경기 고양시", "오늘", "p08_chair.svg", 15));
        list.add(product(9, "맥북 에어 M2 13인치", 950000, "서울 영등포구", "어제", "p09_laptop.svg", 64));
        list.add(product(10, "소니 A7C 미러리스 카메라", 1280000, "서울 송파구", "어제", "p10_camera.svg", 58));

        list.add(product(11, "노스페이스 눕시 패딩", 230000, "서울 강동구", "5분 전", "p11_jacket.svg", 41));
        list.add(product(12, "마샬 액톤 블루투스 스피커", 320000, "경기 수원시", "12분 전", "p12_speaker.svg", 39));
        list.add(product(13, "자바 스프링 실전 교재", 18000, "서울 관악구", "21분 전", "p13_book.svg", 12));
        list.add(product(14, "카시오 지샥 전자시계", 120000, "서울 중구", "35분 전", "p14_watch.svg", 48));
        list.add(product(15, "무인양품 스탠드 조명", 45000, "서울 성북구", "42분 전", "p15_lamp.svg", 26));

        return list;
    }

    private Map<String, Object> product(int productNo,
                                        String productTitle,
                                        int price,
                                        String region,
                                        String regTime,
                                        String imageFile,
                                        int wishCount) {
        Map<String, Object> product = new LinkedHashMap<>();
        product.put("productNo", productNo);
        product.put("productTitle", productTitle);
        product.put("price", price);
        product.put("region", region);
        product.put("regTime", regTime);
        product.put("imageFile", imageFile);
        product.put("wishCount", wishCount);
        return product;
    }
}
