package uz.greenwhite.webstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.greenwhite.webstore.entity.OrderItem;
import uz.greenwhite.webstore.entity.Orders;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
   List<OrderItem> findAllByOrders(Orders orders);

}
