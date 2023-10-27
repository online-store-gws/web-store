package uz.greenwhite.webstore.controller.admin;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.Orders;
import uz.greenwhite.webstore.enums.OrderStatus;
import uz.greenwhite.webstore.service.*;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/orders")
public class OrdersController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;

    @GetMapping()
    public String listPage(
            @RequestParam(name = "status", required = false) OrderStatus orderStatus,
            @RequestParam(name = "filterOrders", required = false) Long filterOrders,
            Principal principal, Model model, Pageable pageable) {

        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("orders", orderService.getOrders(orderStatus, filterOrders, pageable));

        return "admin/data/orders/list";
    }


    @GetMapping("/edit")
    public String editPage(Model model, @RequestParam("id") Long id, Principal principal) {
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
        model.addAttribute("orders", orderService.getById(id));
        model.addAttribute("orderItems", orderItemService.getAllByOrders(id));
        return "admin/data/orders/edit";
    }

    @PostMapping("/edit")
    public String editOrder(@ModelAttribute Orders orders, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            orders.setModifiedBy(user);
            orderService.update(orders);
        }
        return "redirect:/admin/data/orders";
    }

    @GetMapping("delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/admin/data/orders";
    }
}