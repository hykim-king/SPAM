/**
 * 파일명: ReportReason.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-08 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.domain;

public enum ReportReason {
    NON_DELIVERY("상품 미배송"),
    DEFECTIVE_PRODUCT("불량 상품판매"),
    EXTERNAL_PAYMENT("외부 결제 유도"),
    NO_SHOW("노쇼"),
    TRANSACTION_BREACH("거래파기"),
    ABUSIVE_LANGUAGE("욕설 및 언어폭력"),
    PROHIBITED_ITEM("판매금지 물품 등록</option>"),
    PROFESSIONAL_SELLER("전문 업자 의심"),
    FAKE_PRODUCT("허위성 매물"),
    CATEGORY_VIOLATION("카테고리 위반"),
    HARMFUL_CONTENT("음란물 및 유해콘텐츠 게시"),
    SPAM_ADVERTISEMENT("광고 및 홍보 도배"),
    PRIVACY_VIOLATION("개인정보 침해 및 유출"),
    ACCOUNT_THEFT("계정도용");

    private final String description;

    ReportReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // JSP에서 넘어온 한글 텍스트(description)를 가지고 Enum 객체를 찾아내는 유틸리티 메서드
    public static ReportReason fromDescription(String description) {
        for (ReportReason reason : ReportReason.values()) {
            if (reason.getDescription().equals(description)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("올바르지 않은 신고 사유입니다: " + description);
    }
}