package uz.greenwhite.webstore.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.dto.CartDto;
import uz.greenwhite.webstore.entity.*;
import uz.greenwhite.webstore.service.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import uz.greenwhite.webstore.util.CookieUtil;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
public class ClientController {
    private final CategoryService categoryService;

    private final CompanyDetailsService detailsService;

    private final ProductService productService;

    private final CartService cartService;

    private final OrderService orderService;

    @GetMapping
    public String list(Model model, Pageable pageable) {
        Page<Product> productPage = productService.getAll(pageable);
        model.addAttribute("products", productPage);
        Page<Category> page = categoryService.getAll(pageable);
        model.addAttribute("categories", page);
        model.addAttribute("elements", page.getTotalElements());
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "index";
    }

    // list
    @GetMapping("/cart")
    public String cartController(Model model, Pageable pageable,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Page<Category> page = categoryService.getAll(pageable);
        model.addAttribute("categories", page);
        model.addAttribute("elements", page.getTotalElements());



        CartDto carts = new CartDto();
        String token = CookieUtil.getSessionCookie(request, response);
        System.out.println(token);
        ArrayList<Cart> cartArrayList = new ArrayList<>(cartService.getAllByToken(token));
        carts.setCartList(cartArrayList);
        model.addAttribute("carts", carts);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@ModelAttribute(name = "carts") CartDto carts, Model model) {
        if(carts.getCartList() != null) {
            for(Cart cart: carts.getCartList()) {
                cartService.update(cart);
            }
        }
        model.addAttribute("carts", carts);

        return "redirect:/cart";
    }

    @GetMapping("/cart/{productId}")
    public String addProduct(@PathVariable Long productId,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        Cart cart = new Cart();
        cart.setToken(CookieUtil.getSessionCookie(request, response));
        cart.setProduct(productService.getById(productId));
        cartService.save(cart);
        System.out.println(cart.getToken());
        return "redirect:/product/" + productService.getById(productId).getName() + "-" + productId;
    }


    //delete
    @GetMapping("/cart/delete/{id}")
    public String delete(@PathVariable Long id) {
        cartService.deleteById(id);

        return "redirect:/cart";
    }


    @GetMapping("/checkout")
    public String checkoutController(Model model, Pageable pageable,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        Page<Category> page = categoryService.getAll(pageable);
        model.addAttribute("order", new Orders());
        model.addAttribute("categories", page);
        model.addAttribute("elements", page.getTotalElements());
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        String token = CookieUtil.getSessionCookie(request, response);
        System.out.println(token);
        ArrayList<Cart> carts = cartService.getAllByToken(token);
        model.addAttribute("carts", carts);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@ModelAttribute(name = "order") Orders order, @ModelAttribute(name = "carts") CartDto carts, Model model, HttpServletRequest request, HttpServletResponse response) {
        orderService.saveNewOrder(order, CookieUtil.getSessionCookie(request, response));
        updateCart(carts, model);
        return "redirect:/shop";
    }


    @GetMapping("/contact")
    public String contactController(Model model, Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        model.addAttribute("categories", page);
        model.addAttribute("elements", page.getTotalElements());
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "contact";
    }

    @GetMapping("/product/{productName}-{productId}")
    public String detailController(@PathVariable String productName, @PathVariable Long productId, Model model) {
        Product productById = productService.getById(productId);
        if (productById == null) {
            return "error";
        }
        model.addAttribute("product", productById);
        return "detail";
    }

    @GetMapping("/shop")
    public String shopController(@RequestParam(name = "id", required = false) Long categoryId,@RequestParam(name = "order", required = false) Long productOrder,
    @RequestParam(name = "from", required = false) Long productFrom,@RequestParam(name = "to", required = false) Long productTo,  Model model, Pageable pageable) {
        model.addAttribute("filterCategoryId", categoryId);
        model.addAttribute("filterFrom", productFrom);
        model.addAttribute("filterTo", productTo);
        model.addAttribute("products", productService.getProduct(categoryId, productFrom, productTo, productOrder, pageable));

        Page<Category> page = categoryService.getAll(pageable);
        model.addAttribute("categories", page);
        model.addAttribute("elements", page.getTotalElements());
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "/shop";
    }



}