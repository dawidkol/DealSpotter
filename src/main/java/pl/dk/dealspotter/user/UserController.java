package pl.dk.dealspotter.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dk.dealspotter.promo.PromoService;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.user.dto.UserDto;

import java.util.List;

@Controller
@RequestMapping("/user")
class UserController {
    private final UserService userService;
    private final PromoService promoService;

    public UserController(UserService userService1, PromoService promoService) {
        this.userService = userService1;
        this.promoService = promoService;
    }

    @GetMapping("")
    String username(Authentication authentication, Model model) {
        UserDto user = userService.findUser(authentication.getName()).orElseThrow(SecurityException::new);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/promo/all")
    String userPromos(Model model) {
        List<PromoDto> promos = promoService.findPromosByUsername();
        model.addAttribute("promoList", promos);
        return "promo-list";
    }
}
