package com.micro.repository;

import com.micro.Entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  List<Book> findByTitleContainingIgnoreCase(String title);

  List<Book> findByAuthorContainingIgnoreCase(String author);

  List<Book> findByCategoryContainingIgnoreCase(String category);
}