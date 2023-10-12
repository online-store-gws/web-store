package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.repository.ProductRepository;

@AllArgsConstructor
@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    public Product getById(Long ID) {
        return repository.findById(ID).orElse(null);
    }

    public Product update(Product product) {
        return repository.save(product);
    }

    public Product save(Product product) {
        if(product.getIsActive() == null)
            product.setIsActive(true);
        return repository.save(product);
    }

    public void deleteById(Long ID) {
        repository.deleteById(ID);
    }
}
