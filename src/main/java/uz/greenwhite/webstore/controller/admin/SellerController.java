package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.enums.UserRole;
import uz.greenwhite.webstore.service.UserService;
import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/seller")
public class SellerController {

    private final UserService service;

    @GetMapping()
    public String listPage(Model model , Principal principal, Pageable pageable) {
        Optional<User> data = service.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("seller", service.getAll(pageable));
        return "/admin/data/seller/list";
    }

    @GetMapping("/add")
    public String addPage(Model model, Principal principal) {
        Optional<User> data = service.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("seller", new User());
        model.addAttribute("ROLE_SELLER", UserRole.SELLER);
        model.addAttribute("ROLE_MODERATOR", UserRole.MODERATOR);
        return "/admin/data/seller/add";
    }

    @PostMapping("/add")
    public String addSeller(@ModelAttribute User seller) {
        service.save(seller);
        return "redirect:/admin/data/seller";
    }

    @GetMapping("/edit")
    public String editPage(Model model, Principal principal, @RequestParam("id") Long id) {
        Optional<User> data = service.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        User seller = service.getById(id);
        model.addAttribute("seller", seller);
        return "/admin/data/seller/add";
    }

    @PostMapping("/edit")
    public String editSeller(@ModelAttribute User seller) {
        User existingSeller = service.getById(seller.getUserId());

        if (existingSeller != null) {
            existingSeller.setFirstName(seller.getFirstName());
            existingSeller.setLastName(seller.getLastName());
            existingSeller.setUsername(seller.getUsername());
            existingSeller.setPassword(seller.getPassword());
            existingSeller.setIsActive(seller.getIsActive());
            existingSeller.setRole(seller.getRole());
            service.update(existingSeller);

            return "redirect:/admin/data/seller";
        } else {
            return "errorPage";
        }
    }

    @GetMapping("delete/{id}")
    public String deleteSeller(@PathVariable Long id) {
        try {
            service.deleteById(id);
        } catch (RuntimeException e) {
            User seller = service.getById(id);
            seller.setIsActive(false);
            service.update(seller);
        }
        return "redirect:/admin/data/seller";
    }
}