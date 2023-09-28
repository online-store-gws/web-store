package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.service.ProductService;

@Controller
@AllArgsConstructor
@RequestMapping("admin/data/product")
public class ProductController {

    private final ProductService service;

    @GetMapping()
    public String listPage(Model model, Pageable pageable) {
        model.addAttribute("products", service.getAll(pageable));
        return "admin/data/product/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("product", new Product());
        return "admin/data/product/add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        service.save(product);
        return "redirect:/admin/data/product";
    }
}
