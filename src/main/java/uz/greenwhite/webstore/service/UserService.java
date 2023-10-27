package uz.greenwhite.webstore.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public User save(User user) {
        if (user.getUserId() != null)
            throw new RuntimeException("ID should be a null");

        if (user.getIsActive() == null) user.setIsActive(true);
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User update(User seller){
        if(seller.getUserId() == null)
            throw new RuntimeException("Id shouldn't be null");
        seller.setPassword(encoder.encode(seller.getPassword()));
        return repository.save(seller);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .disabled(! user.getIsActive())
                    .build();

    }
}
