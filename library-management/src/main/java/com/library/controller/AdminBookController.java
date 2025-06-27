package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("pageTitle", "Manage Books");
        return "admin/books/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("pageTitle", "Add Book");
        return "admin/books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add Book");
            return "admin/books/form";
        }
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", "Edit Book");
        return "admin/books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("book") Book book,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Book");
            return "admin/books/form";
        }
        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }
}
