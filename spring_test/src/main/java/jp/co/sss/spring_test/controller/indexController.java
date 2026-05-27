package jp.co.sss.spring_test.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.spring_test.entity.Product;
import jp.co.sss.spring_test.entity.Review;
import jp.co.sss.spring_test.form.ReviewForm;
import jp.co.sss.spring_test.repository.CategoryRepository;
import jp.co.sss.spring_test.repository.ProductRepository;
import jp.co.sss.spring_test.repository.ReviewRepository;
import jp.co.sss.spring_test.repository.SalesItemRepository;

@Controller
public class indexController {
	
	@Value("${upload.path}")
    private String uploadPath;
	
	@Autowired
	SalesItemRepository salesItemRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	//商品検索
	@RequestMapping(path = "/product/search")
	public String search(String keyword, Integer category, Model model) {

	    List<Product> products;

	    if ((keyword == null || keyword.isEmpty()) && category == null) {
	        products = productRepository.findAll();
	    } else if (category == null) {
	        products = productRepository.findByProductNameContaining(keyword);
	    } else if (keyword == null || keyword.isEmpty()) {
	        products = productRepository.findByCategory_CategoryId(category);
	    } else {
	        products = productRepository.findByProductNameContainingAndCategory_CategoryId(keyword, category);
	    }

	    model.addAttribute("products", products);
	    model.addAttribute("categories", categoryRepository.findAll());

	    return "product_list";
	}
	
	//TOP画面
	@RequestMapping(path = "/")
	public String index(HttpSession session, Model model) {
		
	    
		model.addAttribute("salesItems", salesItemRepository.findAll());
		model.addAttribute("categories", categoryRepository.findAll());
		
		return "index";
	}
	
	//商品詳細画面
	@RequestMapping(path = "/product/detail/{id}", method = RequestMethod.GET)
	public String productDetail(@PathVariable Integer id, Model model) {
		
		Product product = productRepository.findById(id).orElse(null);
		
		if (product == null) {
		    return "redirect:/";
		}
		
		model.addAttribute("product", product);
		
		List<Review> reviews = reviewRepository.findByProductId(id);
	    model.addAttribute("reviews", reviews);
	    
	    model.addAttribute("totalExTax", product.getPrice());
		model.addAttribute("totalInTax", product.getTaxPrice());
		
		return "product_detail";
	}
	
	//口コミ投稿フォーム
	@RequestMapping(path = "/product/review/{id}", method = RequestMethod.GET)
	public String reviewInput(@PathVariable Integer id, Model model) {
		
		ReviewForm form = new ReviewForm();
	    form.setProductId(id);

	    model.addAttribute("reviewForm", form);

	    return "review";
	}
	
	//口コミ登録処理
	@RequestMapping(path = "/product/review", method = RequestMethod.POST)
	public String reviewSubmit(
			@Valid ReviewForm form,
	        BindingResult result,
	        MultipartFile file,
	        Model model,
	        RedirectAttributes redirectAttributes) throws Exception{
		
		if (result.hasErrors()) {
	        model.addAttribute("reviewForm", form);
	        return "review";
	    }

		Review review = new Review();
		
		review.setProductId(form.getProductId());
		review.setDummyUserName(form.getName());
		review.setRating(form.getRating());
		review.setComment(form.getComment());
		DateTimeFormatter formatter =
		        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		review.setCreatedAt(
		        LocalDateTime.now().format(formatter)
		);
		
		
		//画像処理
		if(!file.isEmpty()) {

		    // ファイル名被り防止
		    String fileName =
		        System.currentTimeMillis() + "_" + file.getOriginalFilename();

		    // フォルダなかったら作る
		    File uploadDir = new File(uploadPath);

		    if (!uploadDir.exists()) {
		        uploadDir.mkdirs();
		    }

		    File dest = new File(uploadPath, fileName);
		    file.transferTo(dest);
		    review.setReviewImgPath(fileName);

		} else {

		    review.setReviewImgPath("no_image.png");
		}
		
		reviewRepository.save(review);
		
		redirectAttributes.addFlashAttribute("message", "口コミを投稿しました！");
		
		return "redirect:/product/detail/" + form.getProductId();
	}
	

}
