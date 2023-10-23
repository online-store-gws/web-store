package uz.greenwhite.webstore.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.greenwhite.webstore.entity.CompanyDetails;
import uz.greenwhite.webstore.repository.CompanyDetailsRepository;

@Service
@AllArgsConstructor
public class CompanyDetailsService {
    private final CompanyDetailsRepository repository;

    public Page<CompanyDetails> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public CompanyDetails getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("CompanyDetails not found for ID: " + id));
    }
    
    public CompanyDetails save(CompanyDetails details) {
        if(details.getPhone1()!=null)
            details.setPhone1(details.getPhone1());

        if(details.getPhone2()!=null)
            details.setPhone2(details.getPhone2());
        return repository.save(details);
    }

    public CompanyDetails update(CompanyDetails details) {
        return repository.save(details);
    }
}