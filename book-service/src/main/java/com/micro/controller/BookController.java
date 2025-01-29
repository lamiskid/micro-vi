package com.micro.controller;

import com.micro.Entity.Book;
import com.micro.Service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public List<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{id}")
  public Book getBookById(@PathVariable Long id) {
    return bookService.getBookById(id);
  }

  @PostMapping
  public Book addBook(@RequestBody Book book) {
    return bookService.addBook(book);
  }

  @PutMapping("/{id}")
  public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
    return bookService.updateBook(id, book);
  }

  @DeleteMapping("/{id}")
  public void deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
  }

  @GetMapping("/search")
  public List<Book> searchBooks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) String category) {
    return bookService.searchBooks(title, author, category);
  }


  @PutMapping("/{id}/issue")
  public Book issueBook(@PathVariable Long id, @RequestParam Long userId) {
    return bookService.issueBook(id, userId);
  }

  @PutMapping("/{id}/return")
  public Book returnBook(@PathVariable Long id) {
    return bookService.returnBook(id);
  }
}