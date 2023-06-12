package pl.dk.dealspotter.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dk.dealspotter.promo.PromoDto;
import pl.dk.dealspotter.promo.PromoService;

import java.util.List;

@Controller
class HomeController {

    private final PromoService promoService;


    public HomeController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping("/")
    String home(Model model) {
        List<PromoDto> list = promoService.findAllPromo();
        model.addAttribute("list", list);
        return "index";
    }





}
