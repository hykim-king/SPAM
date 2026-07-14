package com.pcwk.ehr.product.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.category.domain.CategoryVO;
import com.pcwk.ehr.category.service.CategoryService;
import com.pcwk.ehr.product.domain.ProductSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOG = LogManager.getLogger(ProductController.class);
    private static final String SESSION_LOGIN_USER = "loginUser";
    private static final int PAGE_BLOCK_SIZE = 5;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    /** 검색, 카테고리, 지역, 가격, 상태, 정렬, 페이징이 적용되는 전체 상품 목록. */
    @GetMapping("/list.do")
    public String list(@ModelAttribute("search") ProductSearchDTO search, Model model) {
        int totalCnt = productService.totalCnt(search);
        int totalPage = calculateTotalPage(totalCnt, search.getPageSize());
        clampPageNo(search, totalPage);

        model.addAttribute("list", productService.doRetrieve(search));
        model.addAttribute("categoryList", categoryService.doRetrieveParent());
        addPagingModel(model, search, totalCnt, totalPage);
        return "product/productList";
    }

    /** 상품 상세. 로그인한 판매자 본인이 열면 조회수는 증가하지 않는다. */
    @GetMapping("/view.do")
    public String view(@RequestParam("productNo") int productNo, HttpSession session, Model model) {
        ProductVO param = new ProductVO();
        param.setProductNo(productNo);
        UserVO loginUser = getLoginUser(session);
        if (loginUser != null) {
            param.setUserNum(loginUser.getUserNum());
        }

        ProductVO product = productService.doSelectOne(param);
        model.addAttribute("product", product);
        if (product != null) {
            model.addAttribute("seller", userService.getUser(product.getUserNum()));
        }
        return "product/productView";
    }

    /** 2026-07-13 [수정] 비로그인 시 공통 로그인 안내 모달으로 이동하는 상품 등록 화면. */
    @GetMapping("/saveForm.do")
    public String saveForm(HttpSession session, Model model) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }
        model.addAttribute("categoryList", categoryService.doRetrieveParent());
        return "product/productSave";
    }

    /** 판매자 본인의 상품 수정 화면. */
    @GetMapping("/updateForm.do")
    public String updateForm(@RequestParam("productNo") int productNo, HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        ProductVO param = new ProductVO();
        param.setProductNo(productNo);
        param.setUserNum(loginUser.getUserNum());
        ProductVO product = productService.doSelectOne(param);

        if (product == null || !loginUser.getUserNum().equals(product.getUserNum())) {
            return "redirect:/product/list.do";
        }

        model.addAttribute("product", product);
        model.addAttribute("categoryList", categoryService.doRetrieveParent());
        return "product/productUpdate";
    }

    /**
     * 2026-07-13 [수정] 내 상품 화면을 통합 마이페이지로 이동한다.
     * 기존 URL을 사용하는 링크나 즐겨찾기가 깨지지 않도록 redirect는 유지한다.
     */
    @GetMapping("/myList.do")
    public String myList(@RequestParam(value = "status", required = false) String status, HttpSession session) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }

        if ("01".equals(status) || "02".equals(status) || "03".equals(status)) {
            return "redirect:/user/mypage.do?status=" + status;
        }

        return "redirect:/user/mypage.do";
    }

    /**
     * 2026-07-14 [수정] 판매자 프로필의 소유 모듈을 user로 이전한다.
     * 기존 상품 모듈 URL을 사용하던 링크가 깨지지 않도록 새 프로필 주소로 redirect한다.
     */
    @GetMapping("/seller.do")
    public String legacySeller(@RequestParam("userNum") Long userNum) {
        return "redirect:/user/profile.do?userNum=" + userNum;
    }

    /** 대분류 또는 중분류의 사용 중인 자식 카테고리 JSON 조회. */
    @GetMapping("/categoryChild.do")
    @ResponseBody
    public List<CategoryVO> categoryChild(@RequestParam("parentCategoryNo") int parentCategoryNo) {
        return categoryService.doRetrieveChild(parentCategoryNo);
    }

    /** 상품 등록. 판매자 번호는 화면값이 아니라 로그인 세션에서 설정한다. */
    @PostMapping("/doSave.do")
    public String doSave(ProductVO product,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files,
                         HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        product.setUserNum(loginUser.getUserNum());
        productService.doInsert(product, files);
        return "redirect:/product/view.do?productNo=" + product.getProductNo();
    }

    /** 상품 수정. 새 이미지가 있으면 기존 이미지를 교체한다. */
    @PostMapping("/doUpdate.do")
    public String doUpdate(ProductVO product,
                           @RequestParam(value = "files", required = false) List<MultipartFile> files,
                           HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        product.setUserNum(loginUser.getUserNum());
        productService.doUpdate(product, files);
        return "redirect:/product/view.do?productNo=" + product.getProductNo();
    }

    /** 상품 삭제. 로그인 세션 회원번호로 소유권을 검증한다. */
    @PostMapping("/doDelete.do")
    @ResponseBody
    public int doDelete(ProductVO product, HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return 0;
        }
        product.setUserNum(loginUser.getUserNum());
        return productService.doDelete(product);
    }

    /** 거래상태 변경. 로그인 세션 회원번호로 소유권을 검증한다. */
    @PostMapping("/updateStatus.do")
    @ResponseBody
    public int updateStatus(ProductVO product, HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return 0;
        }
        product.setUserNum(loginUser.getUserNum());
        return productService.updateStatus(product);
    }

    @PostMapping("/plusChatCnt.do")
    @ResponseBody
    public int plusChatCnt(ProductVO product) {
        return productService.plusChatCnt(product);
    }

    @PostMapping("/minusChatCnt.do")
    @ResponseBody
    public int minusChatCnt(ProductVO product) {
        return productService.minusChatCnt(product);
    }

    /** 기존 임시 URL과의 호환용 redirect. */
    @GetMapping("/reg.do")
    public String legacyReg() {
        return "redirect:/product/saveForm.do";
    }

    @GetMapping("/detail.do")
    public String legacyDetail(@RequestParam("productNo") int productNo) {
        return "redirect:/product/view.do?productNo=" + productNo;
    }

    @GetMapping("/popular.do")
    public String popular() {
        return "redirect:/product/list.do?sort=popular";
    }

    @GetMapping("/recommend.do")
    public String recommend() {
        return "redirect:/product/list.do?sort=recommend";
    }

    @GetMapping("/latest.do")
    public String latest() {
        return "redirect:/product/list.do?sort=latest";
    }

    private UserVO getLoginUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_LOGIN_USER);
        return value instanceof UserVO ? (UserVO) value : null;
    }

    private int calculateTotalPage(int totalCnt, int pageSize) {
        return totalCnt == 0 ? 0 : (int) Math.ceil(totalCnt / (double) pageSize);
    }

    private void clampPageNo(ProductSearchDTO search, int totalPage) {
        if (totalPage > 0 && search.getPageNo() > totalPage) {
            search.setPageNo(totalPage);
        }
    }

    private void addPagingModel(Model model, ProductSearchDTO search, int totalCnt, int totalPage) {
        int startPage = totalPage == 0 ? 0 : ((search.getPageNo() - 1) / PAGE_BLOCK_SIZE) * PAGE_BLOCK_SIZE + 1;
        int endPage = totalPage == 0 ? 0 : Math.min(startPage + PAGE_BLOCK_SIZE - 1, totalPage);

        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        LOG.debug("상품 목록 검색: {}, totalCnt={}", search, totalCnt);
    }
}
