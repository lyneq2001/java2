package com.library.controller;

import com.library.entity.Reservation;
import com.library.service.ReservationService;
import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        model.addAttribute("pageTitle", "Manage Reservations");
        return "admin/reservations/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("reservation", reservationService.getReservation(id));
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("pageTitle", "Edit Reservation");
        return "admin/reservations/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        reservation.setId(id);
        reservationService.updateReservation(id, reservation);
        return "redirect:/admin/reservations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/admin/reservations";
    }
}
