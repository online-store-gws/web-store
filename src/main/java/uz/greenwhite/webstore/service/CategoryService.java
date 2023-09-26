package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.repository.CategoryRepository;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public Page<Category> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Category save(Category category) {
        if(category.getCategoryId() != null)
            throw new RuntimeException("Id should be a null");

        return repository.save(category);
    }

    public Category update(Category category) {
        if(category.getCategoryId() == null)
            throw new RuntimeException("Id shouldn't be null");

        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void delete(Category category) {
        deleteById(category.getCategoryId());
    }
}
