package uz.greenwhite.webstore.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.greenwhite.webstore.entity.*;
import uz.greenwhite.webstore.service.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;

@Controller
public class ClientController {
    private final CategoryService categoryService;
    private final CompanyDetailsService detailsService;
    private final ProductService product;

    public ClientController(CategoryService categoryService, CompanyDetailsService detailsService, ProductService product) {
        this.categoryService = categoryService;
        this.detailsService = detailsService;
        this.product = product;
    }

    @GetMapping
    public String list(Model model, Pageable pageable) {
        Page<Product> productPage = product.getAll(pageable);
        model.addAttribute("products", productPage);
        Page<Category> page = categoryService.getAll(pageable);
        long elements = page.getTotalElements();
        model.addAttribute("categories", page);
        model.addAttribute("elements", elements);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "index";
    }

    @GetMapping("/cart")
    public String cartController(Model model, Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        long elements = page.getTotalElements();
        model.addAttribute("categories", page);
        model.addAttribute("elements", elements);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "cart";
    }

    @GetMapping("/contact")
    public String contactController(Model model, Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        long elements = page.getTotalElements();
        model.addAttribute("categories", page);
        model.addAttribute("elements", elements);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "contact";
    }

    @GetMapping("/product/{productName}-{productId}")
    public String detailController(@PathVariable String productName, @PathVariable Long productId, Model model, Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        //long elements = page.getTotalElements();
        Product productById = product.getById(productId);
        if (productById == null) {
            return "error";
        }
        //model.addAttribute("elements", elements);
        model.addAttribute("product", productById);
        return "detail";
    }

    @GetMapping("/shop")
    public String shopController(Model model, Pageable pageable) {
        Page<Product> productPage = product.getAll(pageable);
        model.addAttribute("products", productPage);
        Page<Category> page = categoryService.getAll(pageable);
        long elements = page.getTotalElements();
        model.addAttribute("categories", page);
        model.addAttribute("elements", elements);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "shop";
    }

    @GetMapping("/checkout")
    public String checkoutController(Model model, Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        long elements = page.getTotalElements();
        model.addAttribute("categories", page);
        model.addAttribute("elements", elements);
        Page<CompanyDetails> detailsPage = detailsService.getAll(pageable);
        model.addAttribute("details", detailsPage);
        return "checkout";
    }

}