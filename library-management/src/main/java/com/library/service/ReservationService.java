package com.library.service;

import com.library.entity.Reservation;
import com.library.entity.User;
import com.library.entity.Book;
import com.library.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public long countByUser(User user) {
        return reservationRepository.findByUser(user).size();
    }

    public Reservation reserveBook(User user, Book book) {
        if (reservationRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Reservation already exists");
        }
        Reservation reservation = new Reservation(user, book);
        return reservationRepository.save(reservation);
    }
}
