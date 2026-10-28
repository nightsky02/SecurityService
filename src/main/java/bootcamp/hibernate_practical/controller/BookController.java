package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.exceptions.InvalidAuthorNameException;
import bootcamp.hibernate_practical.exceptions.NoSuchBookException;
import bootcamp.hibernate_practical.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponse createBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        if(id < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id must be positive");
        }

        try{
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch(NoSuchBookException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid UpdateBookRequest updateBookRequest) {
        if(id < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id must be positive");
        }

        try{
            return ResponseEntity.ok(this.bookService.updateBook(id, updateBookRequest));
        } catch(NoSuchBookException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if(id < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id must be positive");
        }

        try{
            this.bookService.deleteBook(id);
            return ResponseEntity.ok("Successfully deleted the book with the id " + id);
        } catch(NoSuchBookException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable String author) {
        try{
            return ResponseEntity.ok(bookService.findByAuthor(author));
        } catch(InvalidAuthorNameException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/available")
    public List<BookResponse> getAvailableBooks() {
        return this.bookService.findAvailableBooks();
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<?> getBooksAfterPublicationYear(@PathVariable int year){
        if(year < 0 || year > 9999){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid year. It must be positive and less than 9999");
        }
        return ResponseEntity.ok(bookService.findBooksAfterPublicationYear(year));
    }

    @GetMapping("/total")
    public long getTotalCountOfBooks(){
        return this.bookService.countBooks();
    }

}
