package com.library.service;

import com.library.entity.Book;
import com.library.entity.Rental;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.RentalRepository;
import com.library.repository.ReservationRepository;
import com.library.service.NotificationService;
import com.library.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<Rental> getCurrentRentals(User user) {
        return rentalRepository.findByUserAndReturnedDateIsNull(user);
    }

    @Transactional(readOnly = true)
    public Rental getRental(Long id) {
        return rentalRepository.findById(id).orElseThrow(() -> new RuntimeException("Rental not found"));
    }

    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Transactional(readOnly = true)
    public long countActiveRentals(User user) {
        return rentalRepository.findByUserAndReturnedDateIsNull(user).size();
    }

    @Transactional(readOnly = true)
    public long countOverdueRentals(User user) {
        return rentalRepository.findOverdue(user, LocalDate.now()).size();
    }

    public Rental rentBook(User user, Book book, int weeks) {
        if (!book.isAvailable() || book.getQuantity() <= 0) {
            throw new RuntimeException("Book not available");
        }

        Rental rental = new Rental(user, book, LocalDate.now(), LocalDate.now().plusWeeks(weeks));
        Rental saved = rentalRepository.save(rental);

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public void markReturned(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
        if (rental.getReturnedDate() == null) {
            rental.setReturnedDate(LocalDate.now());
            rentalRepository.save(rental);
            Book book = rental.getBook();
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);

            List<Reservation> reservations = reservationRepository.findByBookOrderByReservationDateAsc(book);
            if (!reservations.isEmpty() && book.isAvailable() && book.getQuantity() > 0) {
                Reservation first = reservations.get(0);
                Rental newRental = new Rental(first.getUser(), book, LocalDate.now(), LocalDate.now().plusWeeks(1));
                rentalRepository.save(newRental);
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);
                reservationRepository.delete(first);
                notificationService.createNotification(first.getUser(),
                        "Reservation for " + book.getTitle() + " has been converted to rental", "/rentals");
            }
        }
    }
}
