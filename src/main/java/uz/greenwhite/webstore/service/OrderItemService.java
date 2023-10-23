package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.OrderItem;
import uz.greenwhite.webstore.repository.*;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderItemService {
    private final OrderItemRepository repository;
    private final OrderRepository orderRepository;

    public List<OrderItem> getAllByOrders(Long orderId) {
        return repository.findAllByOrders(orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Not found")));
    }

    public Page<OrderItem> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public OrderItem getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public OrderItem save(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    public OrderItem update(OrderItem orderItem) {
        if (orderItem.getOrderItemId() == null)
            throw new RuntimeException("Id shouldn't be null");

        return repository.save(orderItem);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void delete(OrderItem orderItem) {
        deleteById(orderItem.getOrderItemId());
    }
}