package com.library.controller;

import com.library.service.RentalService;
import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rentals", rentalService.findAll());
        model.addAttribute("pageTitle", "Manage Rentals");
        return "admin/rentals/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("rental", rentalService.getRental(id));
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("pageTitle", "Edit Rental");
        return "admin/rentals/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute com.library.entity.Rental rental) {
        rental.setId(id);
        rentalService.saveRental(rental);
        return "redirect:/admin/rentals";
    }

    @PostMapping("/{id}/return")
    public String markReturned(@PathVariable Long id) {
        rentalService.markReturned(id);
        return "redirect:/admin/rentals";
    }
}
