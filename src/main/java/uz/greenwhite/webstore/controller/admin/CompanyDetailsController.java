package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import uz.greenwhite.webstore.entity.CompanyDetails;
import uz.greenwhite.webstore.service.CompanyDetailsService;
import uz.greenwhite.webstore.util.ImageUtil;
import uz.greenwhite.webstore.entity.User;
import uz.greenwhite.webstore.service.UserService;

import java.security.Principal;
import java.util.Optional;
@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/companyDetails")
public class CompanyDetailsController {
    private final CompanyDetailsService service;
    private final UserService userService;
    @GetMapping()
    public String listPage(Model model, Principal principal) {
         CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
         model.addAttribute("companyDetails", companyDetails);
         Optional<User> data = userService.findByUsername(principal.getName());
         model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
         return "admin/data/companyDetails/add";
    }
   
    @GetMapping("/edit")
    public String editPage(Model model, Principal principal) {
        Optional<User> data = userService.findByUsername(principal.getName());
        model.addAttribute("name", data.isPresent() ? data.get().getFirstName() : "User");
       CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
       model.addAttribute("companyDetails", companyDetails);
       return "admin/data/companyDetails/add";
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