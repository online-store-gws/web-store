package uz.greenwhite.webstore.service;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.repository.ProductRepository;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Product save(Product product) {
        if (product.getProductId() != null)
            throw new RuntimeException("Id should be a null");
        return repository.save(product);
    }

}
