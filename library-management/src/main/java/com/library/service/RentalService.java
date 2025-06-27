package com.library.service;

import com.library.entity.Rental;
import com.library.entity.User;
import com.library.repository.RentalRepository;
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

    @Transactional(readOnly = true)
    public List<Rental> getCurrentRentals(User user) {
        return rentalRepository.findByUserAndReturnedDateIsNull(user);
    }

    @Transactional(readOnly = true)
    public long countActiveRentals(User user) {
        return rentalRepository.findByUserAndReturnedDateIsNull(user).size();
    }

    @Transactional(readOnly = true)
    public long countOverdueRentals(User user) {
        return rentalRepository.findOverdue(user, LocalDate.now()).size();
    }
}
