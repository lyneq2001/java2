package com.library.controller;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.service.BookService;
import com.library.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @PostMapping("/reviews/add")
    public String addReview(@RequestParam Long bookId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        Book book = bookService.getBook(bookId);
        reviewService.addReview(user, book, rating, comment);
        redirectAttributes.addFlashAttribute("successMessage", "Review added");
        return "redirect:/books/" + bookId + "/details";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        Book book = reviewService.getReview(id).getBook();
        try {
            reviewService.deleteReview(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Review deleted");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books/" + book.getId() + "/details";
    }
}
