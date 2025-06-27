package com.library.controller;

import com.library.entity.User;
import com.library.service.RentalService;
import com.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Profile");
        return "profile";
    }

    @GetMapping("/rentals")
    public String rentals(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("rentals", rentalService.getCurrentRentals(user));
        model.addAttribute("pageTitle", "My Rentals");
        return "rentals";
    }

    @GetMapping("/reservations")
    public String reservations(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("reservations", reservationService.findByUser(user));
        model.addAttribute("pageTitle", "My Reservations");
        return "reservations";
    }
}
