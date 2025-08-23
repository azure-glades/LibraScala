package org.azgl.lbms.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanID;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    private Book book;

    private LocalDate startDate;

    // Default constructor is required by JPA
    public Loan() {
    }

    public Loan(User user, Book book) {
        this.user = user;
        this.book = book;
        this.startDate = LocalDate.now();
    }

    // Getters and setters
    // ...

    public UUID getLoanID() {
        return loanID;
    }

    public void setLoanID(UUID loanID) {
        this.loanID = loanID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}