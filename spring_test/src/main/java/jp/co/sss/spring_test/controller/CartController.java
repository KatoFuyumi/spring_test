package jp.co.sss.spring_test.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.spring_test.entity.CartItem;
import jp.co.sss.spring_test.entity.Order;
import jp.co.sss.spring_test.entity.OrderItem;
import jp.co.sss.spring_test.entity.Product;
import jp.co.sss.spring_test.entity.User;
import jp.co.sss.spring_test.form.PurchaseForm;
import jp.co.sss.spring_test.repository.OrderItemRepository;
import jp.co.sss.spring_test.repository.OrderRepository;
import jp.co.sss.spring_test.repository.ProductRepository;

@Controller
public class CartController {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;
	
	//カートに商品追加
	@RequestMapping(path = "/cart/add", method = RequestMethod.POST)
	public String addToCart(
			Integer productId, 
			Integer quantity, 
			HttpSession session, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		// セッションからカート取得
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 商品取得
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return "redirect:/";
        }
        
        if (product.getStock() <= 0) {
            return "redirect:/";
        }

        if (quantity == null || quantity <= 0) {
            return "redirect:/";
        }

        if (quantity > product.getStock()) {

            redirectAttributes.addFlashAttribute(
                "message",
                "在庫数を超えています"
            );

            return "redirect:/product/detail/" + productId;
        }

        // カートに追加
        boolean found = false;

        for (CartItem item : cart) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.add(newItem);
        }

        // セッションに保存
        session.setAttribute("cart", cart);

        // 合計金額（税込）
        int totalInTax = 0;

        for (CartItem item : cart) {
            totalInTax += item.getProduct().getTaxPrice() * item.getQuantity();
        }
        
        redirectAttributes.addFlashAttribute("message", "カートに追加しました！");
        redirectAttributes.addFlashAttribute("product", product);
        redirectAttributes.addFlashAttribute("quantity", quantity);
        redirectAttributes.addFlashAttribute("totalInTax", totalInTax);

        return "redirect:/cart/added";
    }
	
	@RequestMapping(path = "/cart/added", method = RequestMethod.GET)
	public String cartAdded(Model model) {

	    return "cart_added";
	}
	
	//カートの商品削除
	@RequestMapping(path = "/cart/delete", method = RequestMethod.POST)
	public String deleteCart(Integer productId, HttpSession session) {
		
		User user = (User) session.getAttribute("user");

		if (user == null) {
		    return "redirect:/login";
		}

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if (cart != null) {
	        cart.removeIf(item -> item.getProduct().getProductId().equals(productId));
	    }

	    session.setAttribute("cart", cart);

	    return "redirect:/cart"; 
	}
	
	//カートの商品一覧
	@RequestMapping("/cart")
	public String showCart(HttpSession session, Model model) {
		
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
		
		int totalInTax = 0;
		int totalExTax = 0;
		int count = 0;

		if (cart != null) {
		    for (CartItem item : cart) {
		        totalInTax += item.getProduct().getTaxPrice() * item.getQuantity();
		        totalExTax += item.getProduct().getPrice() * item.getQuantity();
		        count += item.getQuantity();
		    }
		}

        model.addAttribute("cart", cart);
        model.addAttribute("totalInTax", totalInTax);
        model.addAttribute("totalExTax", totalExTax);
        model.addAttribute("count", count); 

        return "cart";
	}
	
	@RequestMapping(path = "/cart/update", method = RequestMethod.POST)
	public String updateCart(Integer productId, Integer quantity, HttpSession session) {

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if (cart != null) {

	        if (quantity == null || quantity <= 0) {
	            // 削除
	            cart.removeIf(item -> item.getProduct().getProductId().equals(productId));

	        } else {
	            // 更新
	            for (CartItem item : cart) {
	                if (item.getProduct().getProductId().equals(productId)) {
	                    item.setQuantity(quantity);
	                    break;
	                }
	            }
	        }
	    }

	    session.setAttribute("cart", cart);

	    return "redirect:/cart";
	}
	
	//単品購入
	@RequestMapping(path = "/purchase/direct", method = RequestMethod.POST)
	public String directPurchase(Integer productId, HttpSession session) {
		
	    Product product = productRepository.findById(productId).orElse(null);
	    
	    if (product == null) {
	        return "redirect:/";
	    }

	    List<CartItem> cart = new ArrayList<>();

	    CartItem item = new CartItem();
	    item.setProduct(product);
	    item.setQuantity(1);

	    cart.add(item);

	    session.setAttribute("cart", cart);

	    return "redirect:/purchase"; 
	}

	//購入詳細画面
	@RequestMapping(path = "/purchase", method = RequestMethod.GET)
	public String showPurchase(HttpSession session, Model model) {

	    User user = (User) session.getAttribute("user");

	    model.addAttribute("user", user);
	    model.addAttribute("purchaseForm", new PurchaseForm());

	    return "purchase";
	}
	
	@RequestMapping(path = "/purchase", method = RequestMethod.POST)
	public String purchase(
	        @Valid @ModelAttribute("purchaseForm") PurchaseForm purchaseForm,
	        BindingResult result,
	        String addressIndex,
	        String address1,
	        String address2,
	        String building1,
	        String building2,
	        String pay,
	        HttpSession session,
	        Model model) {

		User user = (User) session.getAttribute("user");

		if (result.hasErrors()) {

		    model.addAttribute("user", user);

		    return "purchase";
		}

	    String useAddress;

	    if ("1".equals(addressIndex)) {
	        useAddress = address1 + " " + building1;
	    } else {
	        useAddress = address2 + " " + building2;
	    }
	    
	    session.setAttribute("address", useAddress);

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    int totalInTax = 0;
	    int count = 0;

	    if (cart != null) {
	        for (CartItem item : cart) {
	            totalInTax += item.getProduct().getTaxPrice() * item.getQuantity();
	            count += item.getQuantity();
	        }
	    }

	    model.addAttribute("user", user);
	    model.addAttribute("cart", cart);
	    model.addAttribute("address", useAddress);
	    model.addAttribute("count", count);
	    model.addAttribute("totalInTax", totalInTax);

	    return "confirm";
	}
	
	//購入確認画面
	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String complete(HttpSession session, Model model,RedirectAttributes redirectAttributes) {

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
	    
	    User user = (User) session.getAttribute("user");

	    String address = (String) session.getAttribute("address");

	    int totalInTax = 0;
	    int count = 0;

	    if (cart != null) {
	        for (CartItem item : cart) {
	            totalInTax += item.getProduct().getTaxPrice() * item.getQuantity();
	            count += item.getQuantity();
	        }
	    }

	    model.addAttribute("cart", cart);
	    model.addAttribute("address", address);
	    model.addAttribute("totalInTax", totalInTax);
	    model.addAttribute("count", count);
	    
	 // 在庫チェック
	    if (cart != null) {

	        for (CartItem cartItem : cart) {

	            Product product = cartItem.getProduct();

	            if (product.getStock() < cartItem.getQuantity()) {

	            	redirectAttributes.addFlashAttribute(
	            		    "message",
	            		    product.getProductName() + " の在庫が不足しています"
	            		);

	            		return "redirect:/cart";
	            }
	        }
	    }

	    // 注文保存
	    Order order = new Order();

	    order.setUserId(user.getUserId());
	    order.setTotalAmount(totalInTax);
	    order.setStatus("0");

	    orderRepository.save(order);

	    // 注文明細保存 + 在庫減少
	    if (cart != null) {

	        for (CartItem cartItem : cart) {

	            Product product = cartItem.getProduct();

	            OrderItem orderItem = new OrderItem();

	            orderItem.setOrderId(order.getOrderId());
	            orderItem.setProductId(product.getProductId());
	            orderItem.setQuantity(cartItem.getQuantity());
	            orderItem.setPrice(product.getPrice());

	            orderItemRepository.save(orderItem);

	            // 在庫減少
	            product.setStock(
	                product.getStock() - cartItem.getQuantity()
	            );

	            productRepository.save(product);
	        }
	    }

	    //削除
	    session.removeAttribute("cart");

	    return "complete";
	}
}