package com.obss.javasummerschool.bookportal.controller;

import com.obss.javasummerschool.bookportal.exception.ResourceNotFoundException;
import com.obss.javasummerschool.bookportal.model.User;
import com.obss.javasummerschool.bookportal.payload.response.*;
import com.obss.javasummerschool.bookportal.repository.BookRepository;
import com.obss.javasummerschool.bookportal.repository.UserRepository;
import com.obss.javasummerschool.bookportal.security.CurrentUser;
import com.obss.javasummerschool.bookportal.security.UserPrincipal;
import com.obss.javasummerschool.bookportal.service.BookService;
import com.obss.javasummerschool.bookportal.util.PageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public UserController(UserRepository userRepository, BookRepository bookRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long likeCount = bookRepository.countByLikedUsers(user);
        long readCount = bookRepository.countByReadUsers(user);

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), likeCount, readCount);
    }

    @GetMapping("/users/{username}/liked")
    public PagedResponse<BookResponse> getBooksLikedBy(@PathVariable(value = "username") String username,
                                                       @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        return bookService.getBooksLikedBy(username, page, size);
    }


    @GetMapping("/users/{username}/read")
    public PagedResponse<BookResponse> getBooksReadBy(@PathVariable(value = "username") String username,
                                                      @RequestParam(value = "page", defaultValue = PageValidator.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size", defaultValue = PageValidator.DEFAULT_PAGE_SIZE) int size) {
        return bookService.getBooksReadBy(username, page, size);
    }
}
