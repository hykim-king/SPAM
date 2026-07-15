package com.pcwk.ehr.transact.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

// 스프링 프레임워크의 어노테이션 및 도구들 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 사용하는 도메인(VO/DTO) 및 서비스 인터페이스 가져오기
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;
import com.pcwk.ehr.user.domain.UserVO;

// 이 클래스가 컨트롤러임을 명시하고, 모든 URL은 "/transact"로 시작함
@Controller
@RequestMapping("/transact")
public class TransactController {

    // 서비스 계층의 기능을 사용하기 위해 의존성 주입(DI)
    @Autowired
    private TransactService transactService;

    /** 2026-07-14 [수정] 로그인 회원의 구매/판매 거래내역을 type 기준으로 조회한다. */
    @GetMapping("/list.do")
    public String list(TransacHistSearchDTO dto, HttpSession session, Model model) {
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        dto.setUserNum(loginUser.getUserNum());
        int totalCount = transactService.totalCount(dto);
        int totalPage = totalCount == 0 ? 0 : (int) Math.ceil(totalCount / (double) dto.getPageSize());
        if (totalPage > 0 && dto.getPageNo() > totalPage) {
            dto.setPageNo(totalPage);
        }

        List<TransactHistVO> list = transactService.selectTransactListPaged(dto);
        dto.calculatePaging(totalCount);

        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("paging", dto);
        model.addAttribute("type", dto.getType());
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
