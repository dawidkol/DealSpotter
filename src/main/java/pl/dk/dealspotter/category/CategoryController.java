package pl.dk.dealspotter.category;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dk.dealspotter.promo.PromoDto;
import pl.dk.dealspotter.promo.PromoService;

import java.util.List;

@Controller
@RequestMapping("/category")
class CategoryController {

    private final PromoService promoService;

    public CategoryController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping("/{selectCategory}")
    String categoryPromo(@PathVariable String selectCategory, Model model) {
        List<PromoDto> promoList = promoService.findByCategory(selectCategory);
        model.addAttribute("list", promoList);
        model.addAttribute("category", selectCategory);
        return "products-by-category";
    }
}
