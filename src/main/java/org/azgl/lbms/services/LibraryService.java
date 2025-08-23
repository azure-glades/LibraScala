package org.azgl.lbms.services;

import org.azgl.lbms.exception.ResourceNotFoundException;
import org.azgl.lbms.model.Book;
import org.azgl.lbms.model.Loan;
import org.azgl.lbms.model.User;
import org.azgl.lbms.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibraryService {
    private final UserService userService;
    private final BookService bookService;
    private final LoanRepository loanRepository;

    public LibraryService(UserService userService, BookService bookService, LoanRepository loanRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.loanRepository = loanRepository;
    }

    @Transactional
    public void issueItem(UUID bookID, UUID userID) {
        Optional<Book> bookOpt = bookService.searchBooks(bookID);
        if (bookOpt.isEmpty()) {
            throw new ResourceNotFoundException("Book not found with ID: " + bookID);
        }

        Optional<User> userOpt = userService.searchUser(userID);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userID);
        }

        Book book = bookOpt.get();
        User user = userOpt.get();

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available for loan.");
        }

        // Create the new loan and save
        Loan newLoan = new Loan(user, book);
        loanRepository.save(newLoan);

        // Update the book's available copies and save
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.updateBook(book); // The save method handles the update
    }

    @Transactional
    public double returnItem(UUID bookID, UUID userID) {
        Optional<Book> bookOpt = bookService.searchBooks(bookID);
        Optional<User> userOpt = userService.searchUser(userID);

        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Book or User not found.");
        }

        Book book = bookOpt.get();
        User user = userOpt.get();

        // Find the specific loan to be returned
        Optional<Loan> loanOpt = loanRepository.findFirstByUserAndBookOrderByStartDateDesc(user, book);

        if (loanOpt.isEmpty()) {
            throw new ResourceNotFoundException("Loan record not found for this user and book.");
        }

        Loan loan = loanOpt.get();
        double fine = calculateFine(loan);

        // Remove the loan record
        loanRepository.delete(loan);

        // Update the book's available copies
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookService.updateBook(book);

        return fine;
    }

    public double calculateFine(Loan loan){
        long diff = ChronoUnit.DAYS.between(loan.getStartDate(), LocalDate.now());
        if(diff <= 30){
            return 0;
        } else {
            return (diff-30) * 2.5;
        }
    }
}