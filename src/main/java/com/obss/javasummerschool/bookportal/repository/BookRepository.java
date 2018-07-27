package com.obss.javasummerschool.bookportal.repository;

import com.obss.javasummerschool.bookportal.model.Book;
import com.obss.javasummerschool.bookportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long bookId);

    long countByLikedUsers(User user);

    long countByReadUsers(User user);

    Page<Book> findBooksByTitleContaining(String query, Pageable pageable);

    Page<Book> findBooksByAuthors(Long authorId, Pageable pageable);

    Page<Book> findBooksByLikedUsers(String username, Pageable pageable);

    Page<Book> findBooksByReadUsers(String username, Pageable pageable);
}
