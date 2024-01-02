package pl.dk.dealspotter.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dk.dealspotter.promo.PromoService;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.security.SecurityService;
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
    String username(Model model) {
        String currentUsername = SecurityService.findCurrentUsername();
        UserDto user = userService.findByEmail(currentUsername).orElseThrow(SecurityException::new);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/promo/all")
    String userPromos(Model model) {
        String email = SecurityService.findCurrentUsername();
        if (userService.checkCredentials(email, UserService.ADMIN_ROLE)) {
            List<PromoDto> promos = promoService.findAllPromo();
            model.addAttribute("promoList", promos);
        } else {
            List<PromoDto> promos = promoService.findPromosByUsername(email);
            model.addAttribute("promoList", promos);
        }
        return "promo-list";
    }

    @GetMapping("/promo/delete/{id}")
    String deleteUserPromo(@PathVariable Long id) {
        String email = SecurityService.findCurrentUsername();
        promoService.deletePromo(id, email);
        return "redirect:/user/promo/all";
    }

    @GetMapping("/all-users")
    String allUsers(Model model) {
        List<UserDto> allUsers = userService.findAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "all-users";
    }

    @GetMapping("/delete/{username}")
    String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/user/all-users";
    }

}
