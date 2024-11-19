package com.library.applicationstarter.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.applicationstarter.dtos.BookRequestsDTO;
import com.library.applicationstarter.dtos.BooksDTO;

@Component
public interface BooksService {

    public List<BooksDTO> getAllBooks(String pincode,String author,String title,List<String> genres );

    public void addNewBook(BooksDTO bookDetails);

    public List<BooksDTO> getMyBooks();

    public void updateBookDetails(BooksDTO bookDetails);

    public void deleteBook(int bookId);

    public void requestBook(int bookId);

    public List<BookRequestsDTO> getRequestedBooks();

    public List<BookRequestsDTO> getBookRequests();

    public List<BookRequestsDTO> getAllTransactions();

    public void updateRequestStatus(int bookId, int status);

    public List<Integer> getBookCounts();

}
