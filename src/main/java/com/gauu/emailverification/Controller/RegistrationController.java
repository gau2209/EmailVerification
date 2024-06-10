package com.gauu.emailverification.Controller;

import com.gauu.emailverification.event.RegistrationCompleEvent;
import com.gauu.emailverification.event.lisener.RegistrationCompleEventLisener;
import com.gauu.emailverification.registration.RegistrationRequest;
import com.gauu.emailverification.security.password.IResetPasswordTokenService;
import com.gauu.emailverification.security.token.IVerificationTokenService;
import com.gauu.emailverification.security.token.Token;
import com.gauu.emailverification.user.IUserService;
import com.gauu.emailverification.user.User;
import com.gauu.emailverification.utilities.Urlutil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher appEventPublisher;
    private final IVerificationTokenService verificationTokenService;
    private final IResetPasswordTokenService resetPasswordTokenService;
    private final RegistrationCompleEventLisener eventLisener;


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

    @GetMapping("/forgot-password-request")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String resetPassWordRequest(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/registration/forgot-password-request?not-Found";
        }
        String passwordRequestToken = UUID.randomUUID().toString();
        resetPasswordTokenService.createPasswordResetTokenForUser(user, passwordRequestToken);
        String url = Urlutil.getApplicationUrl(request) + "/registration/password-reset-form?token=";
        try {
            eventLisener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request, Model model) {
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = this.resetPasswordTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "redirect:/error?invalid_token";
        }
        Optional<User> user = this.resetPasswordTokenService.findUserByPasswordResetToken(theToken);
        if (user.isPresent()) {
            resetPasswordTokenService.resetPassword(user.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
