package com.library.controller;

import com.library.entity.User;
import com.library.entity.Book;
import com.library.service.BookService;
import com.library.service.ReservationService;
import com.library.service.RentalService;
import com.library.service.ReviewService;
import com.library.service.UserService;
import com.library.service.ViewedBookService;
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

    @Autowired
    private BookService bookService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ViewedBookService viewedBookService;

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "books/list";
    }

    @GetMapping("/books")
    public String books(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("books", bookService.searchBooks(search));
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Książki");
        return "books/list";
    }

    @GetMapping("/books/{id}/details")
    public String bookDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", "Szczegóły książki");
        model.addAttribute("reviews", reviewService.findByBook(book));

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
                viewedBookService.recordView(user, book);
            }
        }

        return "books/details";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String q, Model model) {
        model.addAttribute("books", bookService.searchBooks(q));
        model.addAttribute("search", q);
        model.addAttribute("pageTitle", "Wyniki wyszukiwania");

        return "books/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("pageTitle", "Panel użytkownika");

        model.addAttribute("activeRentalsCount", rentalService.countActiveRentals(user));
        model.addAttribute("reservationsCount", reservationService.countByUser(user));
        model.addAttribute("reviewsCount", reviewService.countByUser(user));
        model.addAttribute("overdueCount", rentalService.countOverdueRentals(user));
        model.addAttribute("currentRentals", rentalService.getCurrentRentals(user));
        model.addAttribute("recentBooks", viewedBookService.getRecentViews(user));

        return "dashboard";
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