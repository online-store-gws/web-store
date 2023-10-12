package uz.greenwhite.webstore.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.greenwhite.webstore.entity.Category;
import uz.greenwhite.webstore.entity.CompanyDetails;
import uz.greenwhite.webstore.entity.Product;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.enums.UserRole;
import uz.greenwhite.webstore.repository.CompanyDetailsRepository;
import uz.greenwhite.webstore.repository.UserRepository;
import uz.greenwhite.webstore.service.CategoryService;
import uz.greenwhite.webstore.service.CompanyDetailsService;
import uz.greenwhite.webstore.service.ProductService;

@Component
@AllArgsConstructor
public class AppInit implements ApplicationRunner {
    //
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final CategoryService categoryService;
    private final CompanyDetailsService detailsService;

    private final ProductService productService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CompanyDetails details=new CompanyDetails();
        details.setAddress("Komolon");
        details.setCompanyName("Anor");
        details.setEmail("anor@gmail.com");
        details.setInstagramUrl("https://www.instagram.com/");
        details.setLocationUrl("https://www.google.com/maps/place/%D0%9E%D0%9E%D0%9E+%C2%ABGreen+White+Solutions%C2%BB/@41.3112404,69.2359773,17z/data=!3m1!4b1!4m6!3m5!1s0x38ae8adecee8bf99:0x9931414776c08512!8m2!3d41.3112364!4d69.2385522!16s%2Fg%2F11g_3xjb8?entry=ttu");
        details.setTelegramUrl("https://t.me/verifix");
        details.setPhone1("99 251-51-01");
        details.setPhone2("90 120-51-01");
        detailsService.save(details);

        Category category = new Category();
        category.setCategoryName("Texnika");
        categoryService.save(category);
        category.setCategoryId(null);
        category.setCategoryName("Mebel");
        category.setIsActive(false);
        categoryService.save(category);

        Product product = new Product();
        product.setPhoto("1.jpeg");
        product.setIsActive(true);
        product.setQuantity(100);
        product.setName("Samsung");
        product.setPrice(1000L);
        product.setDescription("Made in <b> North Korea )</b>");
        product.setCategory(category);
        productService.save(product);

        Product product2 = new Product();
        product2.setPhoto("2.jpeg");
        product2.setIsActive(true);
        product2.setQuantity(0);
        product2.setName("Webstore");
        product2.setPrice(5000L);
        product2.setDescription("Made in <i> Green White )</i>");
        product2.setCategory(category);
        productService.save(product2);

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Ilon");
            admin.setFirstName("Mask");
            admin.setRole(UserRole.MODERATOR);
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("123"));
            admin.setIsActive(true);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("seller").isEmpty()) {
            User seller = new User();
            seller.setFirstName("Stive");
            seller.setFirstName("Jobs");
            seller.setRole(UserRole.SELLER);
            seller.setUsername("seller");
            seller.setPassword(encoder.encode("123"));
            seller.setIsActive(true);
            userRepository.save(seller);
        }


    }
}