package org.azgl.lbms.api;

import jakarta.validation.Valid;
import org.azgl.lbms.exception.ResourceNotFoundException;
import org.azgl.lbms.model.Book;
import org.azgl.lbms.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/books")
@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@Valid @RequestBody Book book) {
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable UUID id) {
        return bookService.searchBooks(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    @GetMapping("/byTitle/{title}")
    public Book getBookByTitle(@PathVariable String title) {
        return bookService.searchBook(title)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with title: " + title));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyBookCount(
            @PathVariable UUID id,
            @RequestParam int count,
            @RequestParam(defaultValue = "true") boolean increment) {

        boolean success = increment ?
                bookService.increaseCount(id, count) :
                bookService.decreaseCount(id, count);

        if (!success) {
            throw new IllegalStateException("Failed to modify book count.");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}