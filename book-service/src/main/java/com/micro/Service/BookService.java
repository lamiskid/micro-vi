package com.micro.Service;


import com.micro.Entity.Book;
import com.micro.model.User;
import com.micro.repository.BookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private RestTemplate restTemplate;



  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book getBookById(Long id) {
    return bookRepository.findById(id).orElseThrow(() ->
        new RuntimeException("Book not found"));
  }

  public Book addBook(Book book) {
    book.setAvailable(true);
    return bookRepository.save(book);
  }

  public Book updateBook(Long id, Book book) {
    Book existingBook = getBookById(id);
    existingBook.setTitle(book.getTitle());
    existingBook.setAuthor(book.getAuthor());
    existingBook.setCategory(book.getCategory());
    return bookRepository.save(existingBook);
  }

  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }

  public List<Book> searchBooks(String title, String author, String category) {
    if (title != null)
      return bookRepository.findByTitleContainingIgnoreCase(title);
    if (author != null)
      return bookRepository.findByAuthorContainingIgnoreCase(author);
    if (category != null)
      return bookRepository.findByCategoryContainingIgnoreCase(category);
    return getAllBooks();
  }

  public Book issueBook(Long bookId, Long userId) {
    Book book = getBookById(bookId);
    if (!book.getAvailable()) {
      throw new RuntimeException("Book is already issued");
    }

    String userServiceUrl = "http://USER-SERVICE/api/users/" + userId;
    ResponseEntity<User> response = restTemplate.getForEntity(userServiceUrl, User.class);

    if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
      throw new RuntimeException("User not found");
    }

    book.setAvailable(false);
    book.setUserId(userId);
    return bookRepository.save(book);
  }

  public Book returnBook(Long bookId) {
    Book book = getBookById(bookId);
    if (book.getAvailable()) {
      throw new RuntimeException("Book is already available");
    }

    // Mark book as returned
    book.setAvailable(true);
    book.setUserId(null);
    return bookRepository.save(book);
  }
}