package com.library.service;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.entity.ViewedBook;
import com.library.repository.ViewedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ViewedBookService {
    @Autowired
    private ViewedBookRepository viewedBookRepository;

    public void recordView(User user, Book book) {
        ViewedBook vb = new ViewedBook(user, book);
        viewedBookRepository.save(vb);
    }

    @Transactional(readOnly = true)
    public List<ViewedBook> getRecentViews(User user) {
        return viewedBookRepository.findTop5ByUserOrderByViewedDateDesc(user);
    }
}
