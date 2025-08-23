package org.azgl.lbms.repository;

import org.azgl.lbms.model.Book;
import org.azgl.lbms.model.Loan;
import org.azgl.lbms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findByUser(User user);
    Optional<Loan> findFirstByUserAndBookOrderByStartDateDesc(User user, Book book);
}

