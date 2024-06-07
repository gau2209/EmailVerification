package com.gauu.emailverification.Controller;

import com.gauu.emailverification.event.RegistrationCompleEvent;
import com.gauu.emailverification.registration.RegistrationRequest;
import com.gauu.emailverification.security.token.IVerificationTokenService;
import com.gauu.emailverification.security.token.Token;
import com.gauu.emailverification.user.IUserService;
import com.gauu.emailverification.user.User;
import com.gauu.emailverification.utilities.Urlutil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher appEventPublisher;
    private final IVerificationTokenService verificationTokenService;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration, HttpServletRequest httpServlet) {
        User user = this.userService.registerUser(registration);
        //publish the verification email event
        appEventPublisher.publishEvent(new RegistrationCompleEvent(user, Urlutil.getApplicationUrl(httpServlet)));
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<Token> theToken = this.verificationTokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }

        String verifiTokenResult = this.verificationTokenService.validateToken(String.valueOf(token));
        switch (verifiTokenResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
}
