package uz.greenwhite.webstore.controller.admin;


import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.service.CategoryService;
import uz.greenwhite.webstore.service.ProductService;
import uz.greenwhite.webstore.util.ImageUtil;

import java.io.*;


@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/product")
public class ProductController {

    private final ProductService service;

    private final CategoryService categoryService;

    @GetMapping()
    public String listPage(Model model, Pageable pageable) {
        model.addAttribute("products", service.getAll(pageable));
        return "/admin/data/product/list";
    }


    @GetMapping("/add")
    public String addPage(Model model, Pageable pageable) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll(pageable));
        return "/admin/data/product/add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file) throws IOException {
        product = service.save(product);

        product.setPhoto(ImageUtil.saveImage(
                product.getClass().getSimpleName(),
                product.getProductId().toString(), file));

        service.update(product);

        return "redirect:/admin/data/product";
    }

    @GetMapping("/edit")
    public String editPage(Model model, @RequestParam("id") Long id, Pageable pageable) {
        model.addAttribute("product", service.getById(id));
        model.addAttribute("categories", categoryService.getAll(pageable));
        return "admin/data/product/add";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product updatedProduct, @RequestParam("file") MultipartFile file) throws IOException {
        Product originalProduct = service.getById(updatedProduct.getProductId());
        Integer currentQuantity = originalProduct.getQuantity();
        if (updatedProduct.getAddQuantity() != null && updatedProduct.getAddQuantity() > 0) {
            if (updatedProduct.getRemoveQuantity() != null && updatedProduct.getRemoveQuantity() > 0) {
                updatedProduct.setAddQuantity(updatedProduct.getAddQuantity() - updatedProduct.getRemoveQuantity());
                updatedProduct.setRemoveQuantity(0);
            }
            currentQuantity = (currentQuantity != null) ? currentQuantity + updatedProduct.getAddQuantity() : updatedProduct.getAddQuantity();
        } else if (updatedProduct.getRemoveQuantity() != null && updatedProduct.getRemoveQuantity() > 0) {
            currentQuantity = (currentQuantity != null) ? Math.max(0, currentQuantity - updatedProduct.getRemoveQuantity()) : 0;
        }

        BeanUtils.copyProperties(updatedProduct, originalProduct, "productId");
        originalProduct.setQuantity(currentQuantity);
        originalProduct.setPhoto(
                ImageUtil.saveImage(originalProduct.getClass().getSimpleName(),
                originalProduct.getProductId().toString(), file));
        service.update(originalProduct);

        return "redirect:/admin/data/product";
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        try {
            service.deleteById(id);
        } catch (RuntimeException e) {
            Product product = service.getById(id);
            product.setIsActive(false);
            service.update(product);
        }

        return "redirect:/admin/data/product";
    }

    @GetMapping("/image/{id}")
    public void image(@PathVariable Long id, HttpServletResponse response) {
        ImageUtil.getImage(Product.class.getSimpleName(), service.getById(id).getPhoto(), response);
    }
}