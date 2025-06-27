package com.library.controller;

import com.library.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        model.addAttribute("pageTitle", "Manage Reviews");
        return "admin/reviews/list";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }
}
