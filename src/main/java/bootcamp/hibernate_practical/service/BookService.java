package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exceptions.InvalidAuthorNameException;
import bootcamp.hibernate_practical.exceptions.NoSuchBookException;
import bootcamp.hibernate_practical.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getPublicationYear(),
                true
        );
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BookResponse getBookById(Long id) {
        Book book;
        try {
            book = this.tryToGetBookById(id);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            throw new NoSuchBookException("Cannot find the book with id " + id);
        }

        return mapToResponse(book);
    }

    @Transactional
    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Book book;
        try {
            book = this.tryToGetBookById(id);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            throw new NoSuchBookException("Cannot find the book with id " + id);
        }

        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setGenre(request.getGenre());
        book.setPublicationYear(request.getPublicationYear());
        book.setAvailable(request.isAvailable());

        this.bookRepository.save(book); // actually we can skip this operation
        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        Book book;

        try {
            book = this.tryToGetBookById(id);
        } catch (IllegalArgumentException | NoSuchElementException ex) {
            // handle two exceptions at one time not to make the code more complex
            throw new NoSuchBookException("Cannot find a book with the id " + id);
        }

        this.bookRepository.delete(book);
    }

    public List<BookResponse> findByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new InvalidAuthorNameException("Author name must be the right string, not empty");
        }
        List<Book> result = this.bookRepository.findByAuthorContainingIgnoreCase(author);
        return result.stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks() {
        List<Book> result = this.bookRepository.findByAvailableTrue();
        return result.stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findBooksAfterPublicationYear(int year) {
        List<Book> result = this.bookRepository.findByPublicationYearGreaterThan(year);
        return result.stream().map(this::mapToResponse).toList();
    }

    public long countBooks() {
        return this.bookRepository.count();
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationYear(),
                book.isAvailable()
        );
    }

    // Help method not to duplicate the logic of getting a book by id from the db.
    private Book tryToGetBookById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Id cannot be null or negative");
        }

        return this.bookRepository.findById(id).orElseThrow();
    }
}
