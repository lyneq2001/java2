package com.library.controller;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.service.BookService;
import com.library.service.RentalService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private BookService bookService;

    @PostMapping("/rentals/rent")
    public String rentBook(@RequestParam Long bookId,
                           @RequestParam int weeks,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        Book book = bookService.getBook(bookId);
        try {
            rentalService.rentBook(user, book, weeks);
            redirectAttributes.addFlashAttribute("successMessage", "Book rented successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books/" + bookId + "/details";
    }
}
