package com.pcwk.ehr.product.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.category.domain.CategoryVO;
import com.pcwk.ehr.category.service.CategoryService;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
    Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

//    /** 목록 */
//    @GetMapping("/list.do")
//    public String list(Model model) {
//        List<ProductVO> list = productService.doRetrieve();
//        model.addAttribute("list", list);
//        return "product/productList";
//    }

    /** 상세 (상품+카테고리경로+이미지 다 담겨서 옴) */
    @GetMapping("/view.do")
    public String view(ProductVO product, Model model) {
        ProductVO outVO = productService.doSelectOne(product);
        model.addAttribute("product", outVO);
        return "product/productView";
    }

    /** 등록 폼 (대분류 목록 같이 내려줌) */
    @GetMapping("/saveForm.do")
    public String saveForm(Model model) {
        model.addAttribute("categoryList", categoryService.doRetrieveParent());
        return "product/productSave";
    }

    /** 수정 폼 */
    @GetMapping("/updateForm.do")
    public String updateForm(ProductVO product, Model model) {
        model.addAttribute("product", productService.doSelectOne(product));
        model.addAttribute("categoryList", categoryService.doRetrieveParent());
        return "product/productUpdate";
    }

    /** 자식 카테고리 조회 (중분류/소분류 공용, AJAX용) */
    @GetMapping("/categoryChild.do")
    @ResponseBody
    public List<CategoryVO> categoryChild(int parentCategoryNo) {
        return categoryService.doRetrieveChild(parentCategoryNo);
    }

    /** 등록 (이미지 파일 포함) */
    @PostMapping("/doSave.do")
    public String doSave(ProductVO product,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        productService.doInsert(product, files);
        return "redirect:/product/list.do";
    }

    /** 수정 */
    @PostMapping("/doUpdate.do")
    public String doUpdate(ProductVO product) {
        productService.doUpdate(product);
        return "redirect:/product/view.do?productNo=" + product.getProductNo();
    }

    /** 삭제 (성공건수 반환) */
    @PostMapping("/doDelete.do")
    @ResponseBody
    public int doDelete(ProductVO product) {
        return productService.doDelete(product);
    }

    /** 거래상태 변경 */
    @PostMapping("/updateStatus.do")
    @ResponseBody
    public int updateStatus(ProductVO product) {
        return productService.updateStatus(product);
    }

    /** 채팅 수 +1 */
    @PostMapping("/plusChatCnt.do")
    @ResponseBody
    public int plusChatCnt(ProductVO product) {
        return productService.plusChatCnt(product);
    }

    /** 채팅 수 -1 */
    @PostMapping("/minusChatCnt.do")
    @ResponseBody
    public int minusChatCnt(ProductVO product) {
        return productService.minusChatCnt(product);
    }
}