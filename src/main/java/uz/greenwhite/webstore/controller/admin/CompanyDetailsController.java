package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import uz.greenwhite.webstore.entity.CompanyDetails;
import uz.greenwhite.webstore.service.CompanyDetailsService;
import uz.greenwhite.webstore.util.ImageUtil;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/companyDetails")
public class CompanyDetailsController {
    private final CompanyDetailsService service;

    @GetMapping()
    public String listPage(Model model, Pageable pageable) {
         CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
         model.addAttribute("companyDetails", companyDetails);
         return "/admin/data/companyDetails/add";
    }
   
    @GetMapping("/edit")
    public String editPage(Model model) {
       CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
       model.addAttribute("companyDetails", companyDetails);
       return "/admin/data/companyDetails/add";
    }
    
    @PostMapping("/edit")
    public String editCompanyDetails(@ModelAttribute CompanyDetails companyDetails, @RequestParam("file") MultipartFile file) throws IOException {
        CompanyDetails existingCompanyDetails = service.getById(1L); // Retrieve the existing CompanyDetails entity with ID 1
        companyDetails.setCompanyDetailsId(existingCompanyDetails.getCompanyDetailsId()); // Set the ID to 1
        companyDetails.setLogoPath(ImageUtil.saveImage(
                companyDetails.getClass().getSimpleName(),
                companyDetails.getCompanyDetailsId().toString(),
                file));
        service.update(companyDetails);


        return "redirect:/admin/data/companyDetails";
    }
  
    @GetMapping("/image")
    public void image(HttpServletResponse response) {
        ImageUtil.getImage(CompanyDetails.class.getSimpleName(), service.getById(1L).getLogoPath(), response);
    }
}