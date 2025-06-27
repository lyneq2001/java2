package com.library.repository;

import com.library.entity.Rental;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserAndReturnedDateIsNull(User user);

    @Query("SELECT r FROM Rental r WHERE r.user = :user AND r.returnedDate IS NULL AND r.dueDate < :now")
    List<Rental> findOverdue(@Param("user") User user, @Param("now") LocalDate now);
}
