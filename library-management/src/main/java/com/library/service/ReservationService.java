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
            throw new RuntimeException("Rezerwacja juz istnieje");
        }
        Reservation reservation = new Reservation(user, book);
        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));
    }

    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));
        existing.setBook(updated.getBook());
        existing.setUser(updated.getUser());
        existing.setReservationDate(updated.getReservationDate());
        return reservationRepository.save(existing);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public void cancelReservation(Long id, User user) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));
        if (!reservation.getUser().equals(user)) {
            throw new RuntimeException("Brak uprawnien");
        }
        reservationRepository.delete(reservation);
    }
}
