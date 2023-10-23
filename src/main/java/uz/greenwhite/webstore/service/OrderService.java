package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Cart;
import uz.greenwhite.webstore.entity.OrderItem;
import uz.greenwhite.webstore.entity.Orders;
import uz.greenwhite.webstore.repository.OrderRepository;
import uz.greenwhite.webstore.enums.OrderStatus;

import java.util.List;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    public List<Orders> getAllOrdersByStatus(OrderStatus status){
        return repository.findAllByStatus(status);
    }

    public List<Orders> getAllByStatusOrderByCreatedOnAsc(OrderStatus status) {
        return repository.findAllByStatusOrderByCreatedOnAsc(status);
    }

    public List<Orders> getAllByStatusOrderByCreatedOnDesc(OrderStatus status) {
        return repository.findAllByStatusOrderByCreatedOnDesc(status);
    }

    public List<Orders> getAllByOrderByCreatedOnAsc(Pageable pageable) {
        return repository.findAllByOrderByCreatedOnAsc(pageable).getContent();
    }

    public List<Orders> getAllByOrderByCreatedOnDesc(Pageable pageable) {
        return repository.findAllByOrderByCreatedOnDesc(pageable).getContent();
    }

    public List<Orders> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public Orders getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Orders save(Orders orders) {
        if(orders.getOrderId() != null)
            throw new RuntimeException("Id should be a null");
        return repository.save(orders);
    }

    public Orders update(Orders orders) {
        if(orders.getOrderId() == null)
            throw new RuntimeException("Id shouldn't be null");

        for(OrderItem item: orderItemService.getAllByOrders(orders.getOrderId()))
        {
            item.getProduct().setQuantity((int) (item.getProduct().getQuantity() - item.getQuantity()));
            productService.update(item.getProduct());
        }
        return repository.save(orders);
    }

    public void saveNewOrder(Orders order, String token) {
        ArrayList<Cart> carts = cartService.getAllByToken(token);
        OrderItem item;
        order.setStatus(OrderStatus.NEW_ORDER);
        if(carts.isEmpty())
            return;
        save(order);
        for(Cart cart: carts) {
            item = new OrderItem();
            item.setOrders(order);
            item.setQuantity(cart.getCount());
            item.setProduct(cart.getProduct());
            cartService.delete(cart);
            orderItemService.save(item);
        }

    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void delete(Orders orders) {
        deleteById(orders.getOrderId());
    }

    public List<Orders> getOrders(OrderStatus orderStatus, Long filterOrders, Pageable pageable) {
        if(orderStatus==null){
            if(filterOrders==null)
                return getAll(pageable);
            else if(filterOrders==1) return getAllByOrderByCreatedOnAsc(pageable);
            else if(filterOrders==0) return getAllByOrderByCreatedOnDesc(pageable);
        }
        else{
            if(filterOrders==null) return getAllOrdersByStatus(orderStatus);
            else if(filterOrders==1) return getAllByStatusOrderByCreatedOnAsc(orderStatus);
            else if(filterOrders==0) return getAllByStatusOrderByCreatedOnDesc(orderStatus);
        }
        return null;
    }
}
