package com.library.controller;

import com.library.entity.User;
import com.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "expired", required = false) String expired,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        if (expired != null) {
            model.addAttribute("warningMessage", "Your session has expired. Please log in again.");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String firstName,
                                      @RequestParam String lastName,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

        logger.info("Processing registration for username: {}", username);

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Hasla nie sa takie same");
            return "auth/register";
        }

        try {
            // Check if username is available
            if (!userService.isUsernameAvailable(username)) {
                model.addAttribute("errorMessage", "Nazwa uzytkownika juz istnieje");
                return "auth/register";
            }

            // Check if email is available
            if (!userService.isEmailAvailable(email)) {
                model.addAttribute("errorMessage", "Email juz istnieje");
                return "auth/register";
            }

            // Register the user
            User registeredUser = userService.registerUser(username, email, password, firstName, lastName);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Rejestracja zakonczona sukcesem! Mozesz sie teraz zalogowac.");

            logger.info("Successfully registered user: {}", registeredUser.getUsername());
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during user registration", e);
            model.addAttribute("errorMessage", "Wystapil blad podczas rejestracji. Sprobuj ponownie.");
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {

        logger.info("Processing forgot password for email: {}", email);

        try {
            User user = userService.findByEmail(email).orElse(null);

            if (user == null) {
                model.addAttribute("errorMessage", "Nie znaleziono konta z tym adresem e-mail.");
                return "auth/forgot-password";
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Jesli konto z tym adresem istnieje, otrzymasz instrukcje resetu hasla.");

            logger.info("Password reset requested for user: {}", user.getUsername());
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during forgot password process", e);
            model.addAttribute("errorMessage", "Wystapil blad. Sprobuj ponownie.");
            return "auth/forgot-password";
        }
    }

    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam(value = "token", required = false) String token,
                                Model model) {

        if (token == null || token.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Nieprawidlowy token weryfikacyjny.");
            return "auth/verification-error";
        }

        model.addAttribute("successMessage", "Twoje konto zostalo pomyslnie zweryfikowane!");
        return "auth/verification-success";
    }

    @GetMapping("/terms")
    public String termsOfService(Model model) {
        return "auth/terms";
    }

    @GetMapping("/privacy")
    public String privacyPolicy(Model model) {
        return "auth/privacy";
    }
}