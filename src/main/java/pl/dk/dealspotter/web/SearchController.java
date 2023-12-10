package pl.dk.dealspotter.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dk.dealspotter.promo.PromoService;
import pl.dk.dealspotter.promo.dto.PromoDto;

import java.util.List;

@Controller
class SearchController {
    private final PromoService promoService;

    public SearchController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping("/listing")
    String searchPromo(@RequestParam String string, @RequestParam String category, Model model) {
        List<PromoDto> promoList = promoService.findByNameAndCategory(string, category);
        model.addAttribute("list", promoList);
        model.addAttribute("searchString", string);
        return "index";
    }
}
