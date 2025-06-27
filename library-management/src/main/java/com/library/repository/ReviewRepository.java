package com.library.repository;

import com.library.entity.Review;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    long countByUser(User user);
    java.util.List<Review> findByBookOrderByCreatedDateDesc(com.library.entity.Book book);
}
