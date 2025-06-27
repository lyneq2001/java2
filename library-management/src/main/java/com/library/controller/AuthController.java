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
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/register";
        }

        try {
            // Check if username is available
            if (!userService.isUsernameAvailable(username)) {
                model.addAttribute("errorMessage", "Username already exists");
                return "auth/register";
            }

            // Check if email is available
            if (!userService.isEmailAvailable(email)) {
                model.addAttribute("errorMessage", "Email already exists");
                return "auth/register";
            }

            // Register the user
            User registeredUser = userService.registerUser(username, email, password, firstName, lastName);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! You can now log in with your credentials.");

            logger.info("Successfully registered user: {}", registeredUser.getUsername());
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during user registration", e);
            model.addAttribute("errorMessage", "An error occurred during registration. Please try again.");
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
                model.addAttribute("errorMessage", "No account found with this email address.");
                return "auth/forgot-password";
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "If an account with this email exists, you will receive password reset instructions.");

            logger.info("Password reset requested for user: {}", user.getUsername());
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during forgot password process", e);
            model.addAttribute("errorMessage", "An error occurred. Please try again.");
            return "auth/forgot-password";
        }
    }

    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam(value = "token", required = false) String token,
                                Model model) {

        if (token == null || token.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Invalid verification token.");
            return "auth/verification-error";
        }

        model.addAttribute("successMessage", "Your account has been verified successfully!");
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