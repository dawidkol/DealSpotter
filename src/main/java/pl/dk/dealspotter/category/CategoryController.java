package pl.dk.dealspotter.category;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dk.dealspotter.promo.PromoService;
import pl.dk.dealspotter.promo.dto.PromoDto;

import java.util.List;

@Controller
@RequestMapping("/category")
class CategoryController {

    private final PromoService promoService;

    public CategoryController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping("/{categoryName}")
    String categoryPromo(@PathVariable String categoryName, Model model) {
        List<PromoDto> promos;
        String allCategory = CategoryType.All.getDescription();
        if (categoryName.equalsIgnoreCase(allCategory)) {
            promos = promoService.findAllPromo();
            model.addAttribute("list", promos);
        } else  {
            promos = promoService.findByCategory(categoryName);
            model.addAttribute("list", promos);
        }
        return "index";
    }

}
