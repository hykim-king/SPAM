package com.pcwk.ehr.product.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ProductService productService;

    /** 세션에서 로그인 회원번호 (없으면 0 = 비로그인) */
    private int loginNo(HttpSession session) {
        UserVO login = (UserVO) session.getAttribute("loginVO"); // ★ 세션 키 확인
        return (login != null) ? login.getMemberNo() : 0;            // ★ 게터명 확인
    }

    // ───────────────── 화면(JSP) ─────────────────

    /** 목록/검색 */
    @GetMapping("/list.do")
    public String list(Model model) {
        List<ProductVO> list = productService.doRetrieve();
        model.addAttribute("list", list);
        return "product/productList";
    }

    /** 상세 (조회수 증가 ? 본인이면 안 오름) */
    @GetMapping("/view.do")
    public String view(ProductVO product, HttpSession session, Model model) {
        product.setSallerNo(loginNo(session));
        model.addAttribute("product", productService.doSelectOne(product));
        return "product/productView";
    }

    /** 등록 폼 */
    @GetMapping("/saveForm.do")
    public String saveForm() {
        return "product/productSave";
    }

    /** 수정 폼 */
    @GetMapping("/updateForm.do")
    public String updateForm(ProductVO product, Model model) {
        model.addAttribute("product", productService.doSelectOne(product));
        return "product/productUpdate";
    }

    // ───────────────── 저장/수정 (POST → redirect) ─────────────────

    /** 등록 */
    @PostMapping("/doSave.do")
    public String doSave(ProductVO product, HttpSession session) {
        product.setSallerNo(loginNo(session)); // 판매자 = 로그인 회원 (폼값 무시!)
        productService.doInsert(product);
        return "redirect:/product/list.do";
    }

    /** 수정 */
    @PostMapping("/doUpdate.do")
    public String doUpdate(ProductVO product, HttpSession session) {
        product.setSallerNo(loginNo(session));
        productService.doUpdate(product);
        return "redirect:/product/view.do?productNo=" + product.getProductNo();
    }

    // ───────────────── 비동기(JSON) ─────────────────

    /** 삭제 */
    @PostMapping("/doDelete.do")
    @ResponseBody
    public Map<String, Object> doDelete(ProductVO product, HttpSession session) {
        product.setSallerNo(loginNo(session));
        return run(() -> productService.doDelete(product), "삭제");
    }

    /** 거래상태 변경 (01 판매중 / 02 예약중 / 03 판매완료) */
    @PostMapping("/changeStatus.do")
    @ResponseBody
    public Map<String, Object> changeStatus(ProductVO product, HttpSession session) {
        product.setSallerNo(loginNo(session));
        return run(() -> productService.changeStatus(product), "상태변경");
    }

    /** 채팅 수 +1 (채팅방 생성 시) */
    @PostMapping("/plusChatCnt.do")
    @ResponseBody
    public Map<String, Object> plusChatCnt(ProductVO product) {
        return run(() -> productService.plusChatCnt(product), "채팅수 증가");
    }

    /** 채팅 수 -1 (채팅방 삭제 시) */
    @PostMapping("/minusChatCnt.do")
    @ResponseBody
    public Map<String, Object> minusChatCnt(ProductVO product) {
        return run(() -> productService.minusChatCnt(product), "채팅수 감소");
    }

    // ───────────────── 공통 JSON 응답 처리 ─────────────────

    /** 서비스 호출을 try/catch로 감싸 {flag, message} 로 반환 */
    private Map<String, Object> run(IntSupplier action, String label) {
        Map<String, Object> res = new HashMap<>();
        try {
            int flag = action.getAsInt();
            res.put("flag", flag);
            res.put("message", (flag == 1) ? label + " 성공" : label + " 실패");
        } catch (RuntimeException e) {
            log.error("{} 오류: {}", label, e.getMessage());
            res.put("flag", 0);
            res.put("message", e.getMessage());
        }
        return res;
    }
}
