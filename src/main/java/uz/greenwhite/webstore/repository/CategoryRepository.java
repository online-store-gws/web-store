package uz.greenwhite.webstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.greenwhite.webstore.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}