package uz.greenwhite.webstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.greenwhite.webstore.entity.Cart;
import uz.greenwhite.webstore.entity.Product;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByTokenAndProduct(String token, Product product);

    List<Cart> findAllByToken(String token);

    int countByToken(String token);
}
