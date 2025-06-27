package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viewed_books")
public class ViewedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "viewed_date", nullable = false)
    private LocalDateTime viewedDate = LocalDateTime.now();

    public ViewedBook() {}

    public ViewedBook(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDateTime getViewedDate() { return viewedDate; }
    public void setViewedDate(LocalDateTime viewedDate) { this.viewedDate = viewedDate; }
}
