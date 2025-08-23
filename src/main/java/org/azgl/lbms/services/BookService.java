package org.azgl.lbms.services;


import org.azgl.lbms.model.Book;
import org.azgl.lbms.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book){
        Optional<Book> opt = searchBook(book.getTitle());
        if(opt.isEmpty()){
            book.setAvailableCopies(book.getTotalCopies());
            bookRepository.save(book);
        } else {
            opt.get().setTotalCopies(book.getTotalCopies() + opt.get().getTotalCopies());
            opt.get().setAvailableCopies(book.getTotalCopies() + opt.get().getAvailableCopies());
            bookRepository.save(book);
        }
    }

    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    //read
    public Optional<Book> searchBook(String title){
        Optional<Book> opt = bookRepository.findByTitle(title);
        if(opt.isEmpty()){
            System.out.println("Book not found");
        } else {
            System.out.println(opt.get());
        }
        return opt;
    }
    public Optional<Book> searchBooks(UUID lID){
        Optional<Book> opt = bookRepository.findById(lID);
        if(opt.isEmpty()){
            System.out.println("Object not found");
        } else {
            System.out.println(opt.get());
        }
        return opt;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public boolean checkAvailablity(String name){
        Optional<Book> opt = bookRepository.findByTitle(name);
        if(opt.isEmpty() || opt.get().getAvailableCopies() == 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean increaseCount(UUID bID, int count){
        Optional<Book> opt = bookRepository.findById(bID);
        if(opt.isPresent()){
            Book book = opt.get();
            int newTotal = book.getTotalCopies() + count;
            book.setTotalCopies(newTotal);
            bookRepository.save(book); // This saves the updated book
            return true;
        }
        return false;
    }
    public boolean decreaseCount(UUID bID, int count){
        Optional<Book> opt = bookRepository.findById(bID);
        if(opt.isPresent()){
            Book book = opt.get();
            if(book.getTotalCopies() >= count){
                int newTotal = book.getTotalCopies() - count;
                book.setTotalCopies(newTotal);
                bookRepository.save(book);
                return true;
            }
        }
        return false;
    }

    public void deleteBook(UUID bID){ // renamed from deleteLoanable
        bookRepository.deleteById(bID);
    }

    public void deleteBook(String title){ // renamed from deleteLoanable
        Optional<Book> opt = bookRepository.findByTitle(title);
        if(opt.isPresent()){
            bookRepository.deleteById(opt.get().getbID());
        } else {
            System.out.println("Book not found to delete.");
        }
    }
}
