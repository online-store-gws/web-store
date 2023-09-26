package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.service.CategoryService;

@Controller
@AllArgsConstructor
@RequestMapping("admin/data/category")
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public String listPage(Model model, Pageable pageable) {
        model.addAttribute("categories", service.getAll(pageable));
        return "admin/data/category/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("category", new Category());
        return "/admin/data/category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        service.save(category);
        return "redirect:/admin/data/category/add";
    }

    @GetMapping("/edit")
    public String editPage(Model model, @RequestParam("id") Long id) {
        model.addAttribute("category", service.getById(id));
        return "admin/data/category/edit";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        service.update(category);
        return "redirect:admin/data/category";
    }

    @GetMapping("delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:admin/data/category";
    }
}
