package com.library.controller;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.service.BookService;
import com.library.service.ReservationService;
import com.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BookService bookService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/reservations/reserve")
    public String reserveBook(@RequestParam Long bookId,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        Book book = bookService.getBook(bookId);
        try {
            reservationService.reserveBook(user, book);
            notificationService.createNotification(user,
                    "Reserved book: " + book.getTitle(),
                    "/reservations");
            redirectAttributes.addFlashAttribute("successMessage", "Book reserved successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books/" + bookId + "/details";
    }

    @PostMapping("/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        try {
            reservationService.cancelReservation(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation cancelled");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/reservations";
    }
}
