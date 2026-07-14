package com.pcwk.ehr.transact.controller;

import java.util.List;

// 스프링 프레임워크의 어노테이션 및 도구들 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 사용하는 도메인(VO/DTO) 및 서비스 인터페이스 가져오기
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;

// 이 클래스가 컨트롤러임을 명시하고, 모든 URL은 "/transact"로 시작함
@Controller
@RequestMapping("/transact")
public class TransactController {

    // 서비스 계층의 기능을 사용하기 위해 의존성 주입(DI)
    @Autowired
    private TransactService transactService;

    // "/transact/list.do" URL로 GET 요청이 들어오면 이 메서드가 실행됨
    @GetMapping("/list.do")
    public String list(TransacHistSearchDTO dto, // 페이지 번호, 검색어 등이 담긴 객체
                       @RequestParam(value = "status", required = false) String status, // 필터링할 상태값(선택적)
                       Model model) { // JSP로 데이터를 전달하기 위한 모델 객체
        
        // 검색조건 DTO에 상태값을 설정
        dto.setStatus(status); 
        
        // 1. 리스트 조회: 페이징 처리가 된 상품 리스트를 가져옴 (JOIN 된 결과 포함)
        List<ProductVO> list = transactService.selectProductListPaged(dto);
        
        // 2. 전체 건수 조회: 페이징 계산을 위해 현재 검색 조건의 전체 데이터 개수를 가져옴
        int totalCount = transactService.totalCount(dto);
        
        // 3. 페이징 계산: DTO 내부의 메서드를 호출하여 페이지 블록(startPage, endPage 등)을 설정
        dto.calculatePaging(totalCount);
        
        // JSP 화면에서 사용할 데이터를 모델에 담음
        model.addAttribute("list", list);           // 조회된 상품 목록
        model.addAttribute("totalCount", totalCount); // 전체 데이터 개수
        model.addAttribute("paging", dto);          // 페이징 정보(현재 페이지, 시작/끝 페이지)
        model.addAttribute("currentStatus", status); // 현재 선택된 필터 상태
        
        // 최종적으로 보여줄 JSP 페이지 경로 반환
        return "transact/transact_list";
    }

    // 상품 번호를 받아 상세 정보를 JSON 형태로 반환하는 AJAX용 메서드
    @GetMapping("/getDetail.do")
    @ResponseBody
    public TransactHistVO getDetail(@RequestParam("productNo") Long productNo) {
        // 상품 번호를 통해 상세 거래 정보(제목, 가격, 등록일 등)를 조회
        return transactService.selectProductDetail(productNo);
    }
}