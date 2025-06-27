package com.library.service;

import com.library.entity.User;
import com.library.entity.Book;
import com.library.entity.Review;
import com.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public long countByUser(User user) {
        return reviewRepository.countByUser(user);
    }

    public Review addReview(User user, Book book, int rating, String comment) {
        Review r = new Review(user, book, rating, comment);
        return reviewRepository.save(r);
    }

    @Transactional(readOnly = true)
    public java.util.List<Review> findByBook(Book book) {
        return reviewRepository.findByBookOrderByCreatedDateDesc(book);
    }
}
