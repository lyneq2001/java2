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

    @Transactional(readOnly = true)
    public java.util.List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Review getReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono recenzji"));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public void deleteReview(Long id, User user) {
        Review review = getReview(id);
        if (!review.getUser().equals(user)) {
            throw new RuntimeException("Brak uprawnien");
        }
        reviewRepository.delete(review);
    }
}
