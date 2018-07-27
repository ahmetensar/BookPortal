package com.obss.javasummerschool.bookportal.service;

import com.obss.javasummerschool.bookportal.exception.ResourceNotFoundException;
import com.obss.javasummerschool.bookportal.model.Author;
import com.obss.javasummerschool.bookportal.model.Book;
import com.obss.javasummerschool.bookportal.payload.request.BookRequest;
import com.obss.javasummerschool.bookportal.payload.response.BookResponse;
import com.obss.javasummerschool.bookportal.payload.response.PagedResponse;
import com.obss.javasummerschool.bookportal.repository.AuthorRepository;
import com.obss.javasummerschool.bookportal.repository.BookRepository;
import com.obss.javasummerschool.bookportal.util.ModelMapper;
import com.obss.javasummerschool.bookportal.util.PageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public PagedResponse<BookResponse> getAllBooks(int page, int size) {
        PageValidator.validate(page, size);

        Page<Book> books = bookRepository.findAll(titleSortedPageable(page, size));

        return getPagedResponses(books);
    }

    public PagedResponse<BookResponse> getBooksByQuery(String query, int page, int size) {
        PageValidator.validate(page, size);

        Page<Book> books = bookRepository.findBooksByTitleContaining(query, titleSortedPageable(page, size));

        return getPagedResponses(books);
    }

    public PagedResponse<BookResponse> getBooksByAuthorId(Long authorId, int page, int size) {
        PageValidator.validate(page, size);

        Page<Book> books = bookRepository.findBooksByAuthors(authorId, titleSortedPageable(page, size));

        return getPagedResponses(books);
    }

    public PagedResponse<BookResponse> getBooksLikedBy(String username, int page, int size) {
        PageValidator.validate(page, size);

        Page<Book> books = bookRepository.findBooksByLikedUsers(username, titleSortedPageable(page, size));

        return getPagedResponses(books);
    }

    public PagedResponse<BookResponse> getBooksReadBy(String username, int page, int size) {
        PageValidator.validate(page, size);

        Page<Book> books = bookRepository.findBooksByReadUsers(username, titleSortedPageable(page, size));

        return getPagedResponses(books);
    }

    public Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setImageLink(bookRequest.getImageLink());

        bookRequest.getAuthors().forEach(authorId -> {
            Author author = authorRepository.findById(authorId).orElseThrow(
                    () -> new ResourceNotFoundException("Author", "id", authorId));
            book.addAuthor(author);
        });

        return bookRepository.save(book);
    }

    public BookResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", bookId));
        return ModelMapper.mapBookToBookResponse(book);
    }

    private static Pageable titleSortedPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.Direction.ASC, "title");
    }

    private static PagedResponse<BookResponse> getPagedResponses(Page<Book> books) {
        List<BookResponse> bookResponses;

        if (books.getNumberOfElements() == 0) {
            bookResponses = Collections.emptyList();
        } else {
            bookResponses = books.map(ModelMapper::mapBookToBookResponse).getContent();
        }

        return new PagedResponse<>(bookResponses, books.getNumber(),
                books.getSize(), books.getTotalElements(), books.getTotalPages(), books.isLast());
    }
}
