package com.obss.javasummerschool.bookportal.controller;

import com.obss.javasummerschool.bookportal.model.Author;
import com.obss.javasummerschool.bookportal.payload.request.AuthorRequest;
import com.obss.javasummerschool.bookportal.payload.response.ApiResponse;
import com.obss.javasummerschool.bookportal.payload.response.AuthorResponse;
import com.obss.javasummerschool.bookportal.payload.response.PagedResponse;
import com.obss.javasummerschool.bookportal.service.AuthorService;
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
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;


    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @GetMapping
    public PagedResponse<AuthorResponse> getAuthors(@RequestParam(value = "query", required = false) String query,
                                                       @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        if (query == null) {
            return authorService.getAllAuthors(page, size);
        }
        return authorService.getAuthorsByQuery(query, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        Author author = authorService.createAuthor(authorRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{authorId}")
                .buildAndExpand(author.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Author Created Successfully"));
    }

    @GetMapping("/{authorId}")
    public AuthorResponse getAuthorById(@PathVariable Long authorId) {
        return authorService.getAuthorById(authorId);
    }

    @GetMapping("/book/{bookId}")
    public PagedResponse<AuthorResponse> getAuthorsByBookId(@PathVariable Long bookId,
                                                       @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        return authorService.getAuthorsByBookId(bookId, page, size);
    }
}
