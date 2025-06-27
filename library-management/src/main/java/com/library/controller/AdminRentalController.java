package com.library.controller;

import com.library.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rentals", rentalService.findAll());
        model.addAttribute("pageTitle", "Manage Rentals");
        return "admin/rentals/list";
    }

    @PostMapping("/{id}/return")
    public String markReturned(@PathVariable Long id) {
        rentalService.markReturned(id);
        return "redirect:/admin/rentals";
    }
}
