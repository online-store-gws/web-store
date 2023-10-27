package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.service.CategoryService;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin/data/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService service;
    private UserService userService;

    @GetMapping()
    public String listPage(Model model, Principal principal, Pageable pageable) {
        model.addAttribute("categories", service.getAll(pageable));
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        return "/admin/data/category/list";
    }

    @GetMapping("warn/{id}")
    public String warn(@PathVariable Long id, Model model) {
        model.addAttribute("warnCategory", service.getById(id));
        return "/admin/data/category/list";
    }

    @GetMapping("/add")
    public String addPage(Model model, Principal principal) {
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("category", new Category());
        return "/admin/data/category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        service.save(category);
        return "redirect:/admin/data/category";
    }

    @GetMapping("/edit")
    public String editPage(Model model, @RequestParam("id") Long id, Principal principal) {
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("category", service.getById(id));
        return "/admin/data/category/add";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        service.update(category);
        return "redirect:/admin/data/category";
    }

    @GetMapping("delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        try {
            service.deleteById(id);
        } catch (RuntimeException e) {
            Category category = service.getById(id);
            category.setIsActive(false);
            service.update(category);
        }
        return "redirect:/admin/data/category";

    }
}