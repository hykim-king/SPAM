package com.pcwk.ehr.transact.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.service.TransactService;
import com.pcwk.ehr.user.domain.UserVO;

/** 관리자용 전체 상품 현황. 일반 회원 구매/판매내역과 URL을 분리한다. */
@Controller
@RequestMapping("/admin/transact")
public class AdminTransactController {

    private static final String SESSION_LOGIN_USER = "loginUser";
    private static final String ROLE_ADMIN = "02";

    @Autowired
    private TransactService transactService;

    @GetMapping("/list.do")
    public String list(TransacHistSearchDTO search, HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }
        if (!ROLE_ADMIN.equals(loginUser.getUserRole())) {
            return "redirect:/main.do?modal=forbidden";
        }

        int totalCount = transactService.adminProductTotalCount(search);
        int totalPage = totalCount == 0 ? 0 : (int) Math.ceil(totalCount / (double) search.getPageSize());
        if (totalPage > 0 && search.getPageNo() > totalPage) {
            search.setPageNo(totalPage);
        }

        List<ProductVO> list = transactService.selectAdminProductListPaged(search);
        search.calculatePaging(totalCount);

        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("paging", search);
        model.addAttribute("currentStatus", search.getStatus());
        return "admin/transact/transact_list";
    }

    private UserVO getLoginUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_LOGIN_USER);
        return value instanceof UserVO ? (UserVO) value : null;
    }
}
