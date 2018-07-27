package com.obss.javasummerschool.bookportal.controller;

import com.obss.javasummerschool.bookportal.model.Book;
import com.obss.javasummerschool.bookportal.payload.request.BookRequest;
import com.obss.javasummerschool.bookportal.payload.response.ApiResponse;
import com.obss.javasummerschool.bookportal.payload.response.BookResponse;
import com.obss.javasummerschool.bookportal.payload.response.PagedResponse;
import com.obss.javasummerschool.bookportal.service.BookService;
import com.obss.javasummerschool.bookportal.util.PageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping
    public PagedResponse<BookResponse> getBooks(@RequestParam(value = "query", required = false) String query,
                                                       @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        if (query == null) {
            return bookService.getAllBooks(page, size);
        }
        return bookService.getBooksByQuery(query, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = bookService.createBook(bookRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{bookId}")
                .buildAndExpand(book.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Book Created Successfully"));
    }

    @GetMapping("/{bookId}")
    public BookResponse getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/author/{authorId}")
    public PagedResponse<BookResponse> getBooksByAuthor(@PathVariable Long authorId,
                                                        @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        return bookService.getBooksByAuthorId(authorId, page, size);
    }
}
