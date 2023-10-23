package uz.greenwhite.webstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String dashboard(Model model, Principal principal) {
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        return "admin/dashboard";
    }
}