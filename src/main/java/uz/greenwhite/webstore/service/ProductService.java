package uz.greenwhite.webstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.repository.ProductRepository;


@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryService categoryService;

    public List<Product> getAllByPriceGreaterThanOrderByPriceDesc(Long price){
        return repository.findAllByPriceGreaterThanOrderByPriceDesc(price);
    }

    public List<Product> getAllByPriceGreaterThanOrderByPriceAsc(Long price){
        return repository.findAllByPriceGreaterThanOrderByPriceAsc(price);
    }

    public List<Product> getAllByPriceLessThanOrderByPriceDesc(Long price){
        return repository.findAllByPriceLessThanOrderByPriceDesc(price);
    }

    public List<Product> getAllByPriceLessThanOrderByPriceAsc(Long price){
        return repository.findAllByPriceLessThanOrderByPriceAsc(price);
    }

    public List<Product> getByPriceBetweenOrderByPriceDesc(Long minPrice, Long maxPrice){
        return repository.findByPriceBetweenOrderByPriceDesc(minPrice,maxPrice);
    } 

    public List<Product> getByPriceBetweenOrderByPriceAsc(Long minPrice, Long maxPrice){
        return repository.findByPriceBetweenOrderByPriceAsc(minPrice,maxPrice);
    } 

    public List<Product> getAllByCategoryAndPriceGreaterThanOrderByPriceDesc(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceGreaterThanOrderByPriceDesc(categoryService.getById(categoryId),price);
    }

    public List<Product> getAllByCategoryAndPriceGreaterThanOrderByPriceAsc(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceGreaterThanOrderByPriceAsc(categoryService.getById(categoryId),price);
    }

    public List<Product> getAllByCategoryAndPriceLessThanOrderByPriceDesc(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceLessThanOrderByPriceDesc(categoryService.getById(categoryId),price);
    }

    public List<Product> getAllByCategoryAndPriceLessThanOrderByPriceAsc(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceLessThanOrderByPriceAsc(categoryService.getById(categoryId),price);
    }

    public List<Product> getByCategoryAndPriceBetweenOrderByPriceDesc(Long categoryId,Long minPrice, Long maxPrice){
        return repository.findByCategoryAndPriceBetweenOrderByPriceDesc(categoryService.getById(categoryId),minPrice,maxPrice);
    } 

    public List<Product> getByCategoryAndPriceBetweenOrderByPriceAsc(Long categoryId,Long minPrice, Long maxPrice){
        return repository.findByCategoryAndPriceBetweenOrderByPriceAsc(categoryService.getById(categoryId),minPrice,maxPrice);
    } 

    public List<Product> getAllByCategoryAndPriceGreaterThan(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceGreaterThan(categoryService.getById(categoryId),price);
    }

    public List<Product> getAllByCategoryAndPriceLessThan(Long categoryId,Long price){
        return repository.findAllByCategoryAndPriceLessThan(categoryService.getById(categoryId),price);
    }

    public List<Product> getByCategoryAndPriceBetween(Long categoryId,Long minPrice, Long maxPrice){
        return repository.findByCategoryAndPriceBetween(categoryService.getById(categoryId),minPrice,maxPrice);
    } 

    public List<Product> getAllByPriceGreaterThan(Long price){
        return repository.findAllByPriceGreaterThan(price);
    }
    
    public List<Product> getAllByPriceLessThan(Long price){
        return repository.findAllByPriceLessThan(price);
    }

    public List<Product> getByPriceBetween(Long minPrice, Long maxPrice){
        return repository.findByPriceBetween(minPrice,maxPrice);
    }

    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public Page<Product> getAllByOrderByPriceAsc(Pageable pageable) {
        return repository.findAllByOrderByPriceAsc(pageable);
    }
    public Page<Product> getAllByOrderByPriceDesc(Pageable pageable) {
        return repository.findAllByOrderByPriceDesc(pageable);
    }
    public List<Product> getByCategory(Category category) {
        return repository.findAllByCategory(category);
    }
    public List<Product> getByCategoryOrderByPriceAsc(Long categoryId) {
        return repository.findAllByCategoryOrderByPriceAsc(categoryService.getById(categoryId));
    }
    public List<Product> getByCategoryOrderByPriceDesc(Long categoryId) {
        return repository.findAllByCategoryOrderByPriceDesc(categoryService.getById(categoryId));
    }
    public Product getById(Long ID) {
        return repository.findById(ID).orElse(null);
    }

    public Product update(Product product) {
        if(product.getQuantity() <= 0) {
            product.setIsActive(false);
        }
        return repository.save(product);
    }

    public Product save(Product product) {
        if(product.getIsActive() == null)
            product.setIsActive(true);
        if(product.getQuantity() <= 0) {
            product.setIsActive(false);
        }
        return repository.save(product);
    }

    public void deleteById(Long ID) {
        repository.deleteById(ID);
    }

    public List<Product> getProduct(
            Long categoryId,
            Long productFrom,
            Long productTo,
            Long productOrder,
            Pageable pageable
            ) {
        if(categoryId != null) {
            if(productFrom!=null&&productTo==null&&productOrder==null)return getAllByCategoryAndPriceGreaterThan(categoryId,productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==null)return getAllByCategoryAndPriceLessThan(categoryId,productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==null)return getByCategoryAndPriceBetween(categoryId,productFrom,productTo);

            else if(productFrom!=null&&productTo==null&&productOrder==0)return getAllByCategoryAndPriceGreaterThanOrderByPriceDesc(categoryId,productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==0)return getAllByCategoryAndPriceLessThanOrderByPriceDesc(categoryId,productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==0)return getByCategoryAndPriceBetweenOrderByPriceDesc(categoryId,productFrom,productTo);

            else if(productFrom!=null&&productTo==null&&productOrder==1)return getAllByCategoryAndPriceGreaterThanOrderByPriceAsc(categoryId,productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==1)return getAllByCategoryAndPriceLessThanOrderByPriceAsc(categoryId,productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==1)return getByCategoryAndPriceBetweenOrderByPriceAsc(categoryId,productFrom,productTo);

            else if(productOrder==null)return getByCategory(categoryService.getById(categoryId));
            else if(productOrder==1)return getByCategoryOrderByPriceAsc(categoryId);
            else if(productOrder==0)return getByCategoryOrderByPriceDesc(categoryId);
        } else {
            if(productFrom!=null&&productTo==null&&productOrder==null)return getAllByPriceGreaterThan(productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==null)return getAllByPriceLessThan(productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==null)return getByPriceBetween(productFrom,productTo);

            else if(productFrom!=null&&productTo==null&&productOrder==0)return getAllByPriceGreaterThanOrderByPriceDesc(productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==0)return getAllByPriceLessThanOrderByPriceDesc(productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==0)return getByPriceBetweenOrderByPriceDesc(productFrom,productTo);

            else if(productFrom!=null&&productTo==null&&productOrder==1)return getAllByPriceGreaterThanOrderByPriceAsc(productFrom);
            else if(productFrom==null&&productTo!=null&&productOrder==1)return getAllByPriceLessThanOrderByPriceAsc(productTo);
            else if(productFrom!=null&&productTo!=null&&productOrder==1)return getByPriceBetweenOrderByPriceAsc(productFrom,productTo);

            else if(productOrder==null)  return getAll(pageable).getContent();
            else if(productOrder==1)  return getAllByOrderByPriceAsc(pageable).getContent();
            else if(productOrder==0) return getAllByOrderByPriceDesc(pageable).getContent();
        }
        return null;
    }
}
