package com.library.repository;

import com.library.entity.Reservation;
import com.library.entity.User;
import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);

    List<Reservation> findByBookOrderByReservationDateAsc(Book book);

    boolean existsByUserAndBook(User user, Book book);
}
