package pl.dk.dealspotter.web;


import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

@Controller
class RegistrationController {
    private final UserService userService;
    private final Validator validator;

    public RegistrationController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping("/register")
    String registrationForm(Model model) {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        model.addAttribute("user", userRegistrationDto);
        return "register";
    }

    @PostMapping("/register")
    String register(@Valid @ModelAttribute(name = "user") UserRegistrationDto userRegistrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            userService.register(userRegistrationDto);
            return "redirect:/confirmation";
        }
    }

    @GetMapping("/confirmation")
    String confirmation() {
        return "registration-confirmation";
    }
}
