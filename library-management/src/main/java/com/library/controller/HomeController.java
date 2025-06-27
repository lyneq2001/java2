package com.library.controller;

import com.library.entity.User;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "books/list";
    }

    @GetMapping("/books")
    public String books(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "false") boolean availableOnly,
            Model model) {

        model.addAttribute("search", search);
        model.addAttribute("availableOnly", availableOnly);
        model.addAttribute("pageTitle", "Books");

        return "books/list";
    }

    @GetMapping("/books/{id}/details")
    public String bookDetails(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Book Details");

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
        }

        return "books/details";
    }

    @GetMapping("/books/search")
    public String searchBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        model.addAttribute("searchQuery", q);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTitle", "Search Results");

        return "books/search-results";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Dashboard");

        if (user.isAdmin()) {
            return "redirect:/admin/dashboard";
        } else {
            return "dashboard";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You don't have permission to access this resource.");
        return "error";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact");
        return "contact";
    }
}