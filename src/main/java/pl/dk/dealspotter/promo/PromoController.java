package pl.dk.dealspotter.promo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.dealspotter.file.FileStorageRepository;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;

import java.io.IOException;

@Controller
@RequestMapping("/promo")
class PromoController {
    private final PromoService promoService;
    private final FileStorageRepository fileStorageRepository;

    public PromoController(PromoService promoService, FileStorageRepository fileStorageRepository) {
        this.promoService = promoService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @GetMapping("/save")
    String savePromo(@CurrentSecurityContext SecurityContext currentSecurityContext, Model model) {
        if (currentSecurityContext.getAuthentication().isAuthenticated()) {
            SavePromoDto savePromoDto = new SavePromoDto();
            model.addAttribute("promo", savePromoDto);
            return "save-promo";
        } else return "login";
    }

    @PostMapping("/save")
    String savePromo(@Valid @ModelAttribute("promo") SavePromoDto savePromoDto,
                     BindingResult bindingResult, @RequestParam(name = "imageFilename")
                     MultipartFile imageFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "save-promo";
        } else {
            promoService.savePromo(savePromoDto);
            fileStorageRepository.save(imageFile.getOriginalFilename(), imageFile.getInputStream());
            return "redirect:/promo/save/confirmation";
        }
    }

    @GetMapping("/save/confirmation")
    String confirmation() {
        return "save-promo-confirmation";
    }

    @GetMapping("/edit/{id}")
    String editPromo(@PathVariable Long id, Model model) {
        PromoDto promo = promoService.getPromoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("promo", promo);
        return "edit-promo";
    }

    @PostMapping("/update")
    String updatePromo(@Valid @ModelAttribute(name = "promo") SavePromoDto savePromoDto,
                       BindingResult bindingResult,
                       @RequestParam(name = "imageFilename") MultipartFile imageFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "edit-promo";
        } else {
            promoService.updatePromo(savePromoDto);
            fileStorageRepository.save(imageFile.getOriginalFilename(), imageFile.getInputStream());
            return "redirect:/promo/edit/confirmation";
        }
    }

    @GetMapping("/edit/confirmation")
    String editConfirmation() {
        return "edit-promo-confirmation";
    }

    @DeleteMapping("/delete/{id}")
    String deleteUserPromo(@PathVariable Long id) {
        promoService.deletePromo(id);
        return "redirect:/user/promo/all";

    }
}
