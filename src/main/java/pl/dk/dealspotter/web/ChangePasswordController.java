package pl.dk.dealspotter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dk.dealspotter.user.UserService;

@Controller
@RequestMapping("/user")
class ChangePasswordController {

    private final UserService userService;
    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/change-password")
    String changePassword() {
        return "change-password-form";
    }

    @PostMapping("/change-password")
    String changePassword(@RequestParam String newPassword) {
        userService.changeUserPassword(newPassword);
        return "redirect:/logout";
    }
}
