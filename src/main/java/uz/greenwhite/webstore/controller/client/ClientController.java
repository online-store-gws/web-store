package uz.greenwhite.webstore.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping("/")
    public String indexController() {
        return "index";
    }
    @GetMapping("/cart")
    public String cartController() {
        return "cart";
    }
    @GetMapping("/contact")
    public String contactController() {
        return "contact";
    }
    @GetMapping("/detail")
    public String detailController() {
        return "detail";
    }
    @GetMapping("/shop")
    public String shopController() {
        return "shop";
    }
    @GetMapping("/checkout")
    public String checkoutController() {
        return "checkout";
    }

}
