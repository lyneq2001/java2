package com.library.repository;

import com.library.entity.ViewedBook;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewedBookRepository extends JpaRepository<ViewedBook, Long> {
    List<ViewedBook> findTop5ByUserOrderByViewedDateDesc(User user);
}
