package com.library.applicationstarter.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.applicationstarter.dtos.BookRequestsDTO;
import com.library.applicationstarter.dtos.BooksDTO;
import com.library.applicationstarter.service.BooksService;

@RestController
@RequestMapping("/books")
public class BooksController {

    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);

    @Autowired
    private BooksService booksService;

    @GetMapping("/getAvailableBooks")
    public List<BooksDTO> getAvailableBooks(@RequestParam String pincode,
    @RequestParam String author,@RequestParam String title,@RequestParam List<String> genres ) {
        return booksService.getAllBooks(pincode,author,title,genres);
    }

    @PostMapping(value = "/addBook")
    public ResponseEntity<?> addBook(@RequestBody BooksDTO book)  throws JsonProcessingException {
         logger.info("in addBook method..");

        try {
            booksService.addNewBook(book);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok("Book added successfully");
    }

    @GetMapping("/getMyBooks")
    public List<BooksDTO> getMyBooks() {
        return booksService.getMyBooks();
    }

    @PostMapping(value = "/updateBook")
    public ResponseEntity<?> updateBook(@RequestBody BooksDTO book)  throws JsonProcessingException {
         logger.info("in updateBook method..");

        try {
            booksService.updateBookDetails(book);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok("Book updated successfully");
    }

    @PostMapping(value = "/deleteMyBook")
    public ResponseEntity<?> deleteMyBook(@RequestParam int bookId )  throws JsonProcessingException {
         logger.info("in deleteMyBook method..");

        try {
            booksService.deleteBook(bookId);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok("Book saved successfully");
    }

    @PostMapping(value = "/requestBook")
    public void requestBook(@RequestParam int bookId )  throws JsonProcessingException {
         logger.info("in requestBook method..");

        try {
            booksService.requestBook(bookId);
        } catch (Exception e) {
            throw e;
        }

    }

    @GetMapping("/getRequestedBooks")
    public List<BookRequestsDTO> getRequestedBooks( ) {
        return booksService.getRequestedBooks();
    }
  
    @GetMapping("/getBookRequests")
    public List<BookRequestsDTO> getBookRequests( ) {
        return booksService.getBookRequests();
    }

    @GetMapping("/getAllTransactions")
    public List<BookRequestsDTO> getAllTransactions( ) {
        return booksService.getAllTransactions();
    }

    @PostMapping(value = "/updateRequestStatus")
    public ResponseEntity<?> updateRequestStatus(@RequestParam int requestId,@RequestParam int status )  throws JsonProcessingException {
         logger.info("in approveRequest method..");

        try {
            booksService.updateRequestStatus(requestId,status);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok("Book saved successfully");
    }

    @GetMapping("/getBookCounts")
    public List<Integer> getBookCounts( ) {
        try {
            return booksService.getBookCounts();
        } catch (Exception e) {
            throw e;
        }
        
    }

}
