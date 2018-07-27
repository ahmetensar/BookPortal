package com.obss.javasummerschool.bookportal.service;

import com.obss.javasummerschool.bookportal.exception.ResourceNotFoundException;
import com.obss.javasummerschool.bookportal.model.Author;
import com.obss.javasummerschool.bookportal.payload.request.AuthorRequest;
import com.obss.javasummerschool.bookportal.payload.response.AuthorResponse;
import com.obss.javasummerschool.bookportal.payload.response.PagedResponse;
import com.obss.javasummerschool.bookportal.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public PagedResponse<AuthorResponse> getAllAuthors(int page, int size) {
        PageValidator.validate(page, size);

        Page<Author> authors = authorRepository.findAll(nameSortedPageable(page, size));

        return getPagedResponses(authors);
    }

    public PagedResponse<AuthorResponse> getAuthorsByQuery(String query, int page, int size) {
        PageValidator.validate(page, size);

        Page<Author> authors = authorRepository.findAuthorsByNameContaining(query, nameSortedPageable(page, size));

        return getPagedResponses(authors);
    }

    public PagedResponse<AuthorResponse> getAuthorsByBookId(Long bookId, int page, int size) {
        PageValidator.validate(page, size);

        Page<Author> authors = authorRepository.findAuthorsByBooks(bookId, nameSortedPageable(page, size));

        return getPagedResponses(authors);
    }

    public Author createAuthor(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setName(authorRequest.getName());
        author.setDescription(authorRequest.getDescription());

        return authorRepository.save(author);
    }

    public AuthorResponse getAuthorById(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", authorId));
        return ModelMapper.mapAuthorToAuthorResponse(author);
    }

    private static Pageable nameSortedPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.Direction.ASC, "name");
    }

    private static PagedResponse<AuthorResponse> getPagedResponses(Page<Author> authors) {
        List<AuthorResponse> authorResponses;

        if (authors.getNumberOfElements() == 0) {
            authorResponses = Collections.emptyList();
        } else {
            authorResponses = authors.map(ModelMapper::mapAuthorToAuthorResponse).getContent();
        }

        return new PagedResponse<>(authorResponses, authors.getNumber(), authors.getSize(),
                authors.getTotalElements(), authors.getTotalPages(), authors.isLast());
    }
}
