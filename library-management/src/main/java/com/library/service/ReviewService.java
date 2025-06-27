package com.library.service;

import com.library.entity.User;
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
}
