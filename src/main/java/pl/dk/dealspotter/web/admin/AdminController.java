package pl.dk.dealspotter.web.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dk.dealspotter.promo.PromoService;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.user.UserNotFoundException;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;
import pl.dk.dealspotter.user.dto.UserDto;

import java.util.List;

@Controller
@RequestMapping("/admin")
class AdminController {
    private static final String ADMIN_AUTHORITY = "ADMIN";
    private final PromoService promoService;
    private final UserService userService;

    public AdminController(PromoService promoService, UserService userService) {
        this.promoService = promoService;
        this.userService = userService;
    }

    @GetMapping("")
    String admin(Authentication authentication, Model model) {
        UserDto user = userService.findUser(authentication.getName()).orElseThrow(SecurityException::new);
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/promo/all")
    String promosList(Model model) {
        List<PromoDto> allPromo = promoService.findAllPromo();
        SavePromoDto savePromoDto = new SavePromoDto();
        model.addAttribute("promo", savePromoDto);
        model.addAttribute("promoList", allPromo);
        return "promo-list";
    }

    @GetMapping("/promo/delete/{id}")
    String deletePromo(@PathVariable Long id) {
        promoService.deletePromo(id);
        return "redirect:/admin/promo/all";
    }

    @GetMapping("/users/all")
    String allUsers(Model model) {
        if (isAdmin()) {

            List<UserDto> allUsers = userService.findAllUsers();
            model.addAttribute("allUsers", allUsers);
            return "all-users";
        } else throw new SecurityException("Brak uprawnień do strony");
    }

    @GetMapping("/user/delete/{username}")
    String deleteUser(@PathVariable String username) {
        if (isAdmin()) {

            userService.deleteUser(username);
            return "redirect:/admin/users/all";
        }  else throw new SecurityException("Brak uprawnień do strony");
    }

    private boolean isAdmin() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCredentialsDto userCredentialsDto = userService.findCredentialsByEmail(name).orElseThrow(UserNotFoundException::new);
        return userCredentialsDto.getRoles().stream().anyMatch(x -> x.equalsIgnoreCase(ADMIN_AUTHORITY));
    }
}
