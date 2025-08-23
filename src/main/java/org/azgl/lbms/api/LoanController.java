package org.azgl.lbms.api;

import org.azgl.lbms.services.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/v1/loans")
@RestController
public class LoanController {

    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/issue")
    public ResponseEntity<String> issueBook(@RequestParam UUID bookId, @RequestParam UUID userId) {
        libraryService.issueItem(bookId, userId);
        return ResponseEntity.ok("Book issued successfully.");
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam UUID bookId, @RequestParam UUID userId) {
        double fine = libraryService.returnItem(bookId, userId);
        return ResponseEntity.ok("Book returned successfully. Fine: $" + fine);
    }
}