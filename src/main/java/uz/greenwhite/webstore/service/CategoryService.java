package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.repository.CategoryRepository;
import uz.greenwhite.webstore.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    private final ProductRepository productRepository;

    public List<Category> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public Category getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Category save(Category category) {
        if(category.getCategoryId() != null)
            throw new RuntimeException("Id should be a null");

        if(category.getIsActive() == null) category.setIsActive(true);

        return repository.save(category);
    }

    public Category update(Category category) {
        if(category.getCategoryId() == null)
            throw new RuntimeException("Id shouldn't be null");

        for(Product product: productRepository.findAllByCategory(category)) {
            if(product.getQuantity() <= 0)
                product.setIsActive(false);
            else
                product.setIsActive(category.getIsActive());

            productRepository.save(product);
        }

        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void delete(Category category) {
        deleteById(category.getCategoryId());
    }
}
