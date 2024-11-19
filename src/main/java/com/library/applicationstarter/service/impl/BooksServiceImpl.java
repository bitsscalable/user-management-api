package com.library.applicationstarter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.library.applicationstarter.dtos.BookRequestsDTO;
import com.library.applicationstarter.dtos.BooksDTO;
import com.library.applicationstarter.entitys.Books;
import com.library.applicationstarter.entitys.MailTemplates;
import com.library.applicationstarter.entitys.TransactionRequests;
import com.library.applicationstarter.entitys.Users;
import com.library.applicationstarter.repository.BooksRepo;
import com.library.applicationstarter.repository.TransactionReqRepo;
import com.library.applicationstarter.repository.UsersRepo;
import com.library.applicationstarter.service.BooksService;
import com.library.applicationstarter.service.EmailService;
import com.library.applicationstarter.service.SecurityContext;
import com.library.applicationstarter.service.SequenceGeneratorService;

@Service
public class BooksServiceImpl implements BooksService {

    private static final Logger logger = LoggerFactory.getLogger(BooksServiceImpl.class);
        
        @Autowired
        private BooksRepo booksRepo;
    
        @Autowired
        private SecurityContext securityContext;
    
        @Autowired
        private UsersRepo usersRepo;
    
        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private SequenceGeneratorService generatorService;

        @Autowired
        private TransactionReqRepo transactionReqRepo;

        @Autowired
        private EmailService emailService;
    
        @Override
    public List<BooksDTO> getAllBooks(String pincode, String author, String title, List<String> genres) {
    
            Query query = new Query();
    
            query.addCriteria(
                new Criteria().andOperator(
                    new Criteria().orOperator(
                        Criteria.where("title").regex(".*" + Pattern.quote(title.toLowerCase()) + ".*", "i"),
                        Criteria.where("author").regex(".*" + Pattern.quote(author.toLowerCase()) + ".*", "i"),
                        Criteria.where("genres").in(genres),
                        Criteria.where("pincode").is(pincode)
                    ),
                Criteria.where("uploadedBy").ne(securityContext.getLoggedInUsername()) // Exclude books uploaded by logged in user
));

    
            List<Books> books =  mongoTemplate.find(query, Books.class);
    
    
    
            List<BooksDTO> bookDtos = new ArrayList<>();
    
            for(int i=0;i<books.size();i++){
                BooksDTO bean = new BooksDTO();
                BeanUtils.copyProperties(books.get(i), bean);
                bookDtos.add(bean);
            }
    
            for(BooksDTO book: bookDtos){
                Optional<Users> user =  usersRepo.findByUsername(book.getUploadedBy());
                if(user.isPresent()){
                    book.setUploadedBy(user.get().getFirstName()+" "+user.get().getMiddleName()+" "+user.get().getLastName());
                }
            }
    
            return bookDtos;
    
        }
    
        @Override
        public void addNewBook(BooksDTO bookDetails) {
            logger.info("in addNewBook method..");
            try {
                Books book = new Books();
                logger.info("before getting loggedin username...");
                bookDetails.setUploadedBy(securityContext.getLoggedInUsername());
                logger.info("after getting loggedin username...");
    
                BeanUtils.copyProperties(bookDetails, book);
                book.setBookId(generatorService.getNextSequenceId("books"));
                logger.info("saving book details to db...");
                booksRepo.save(book);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while adding new book", e);
            }
    
        }
    
        @Override
        public List<BooksDTO> getMyBooks() {
            logger.info("in getMyBooks method..");
    
            try {
                List<BooksDTO> bookDtos = new ArrayList<>();
                
                Query query = new Query();
                String username = securityContext.getLoggedInUsername();
    
                query.addCriteria(Criteria.
                        where("uploadedBy").is(username));
                        query.addCriteria(Criteria.
                        where("isActive").is(true));
        
                List<Books> books =  mongoTemplate.find(query, Books.class);
    
                for(int i=0;i<books.size();i++){
                    BooksDTO bean = new BooksDTO();
                    BeanUtils.copyProperties(books.get(i), bean);
                    bookDtos.add(bean);
                }
                return bookDtos;
    
        } catch (Exception e) {
           e.printStackTrace();
           throw new Error("Error while retriving your book", e);
        }

    }

        @Override
        public void updateBookDetails(BooksDTO bookDetails) {
            
            try {
                
                Optional<Books> book = booksRepo.findByBookId(bookDetails.getBookId());

                if(book.isPresent()){
                    Books bk = new Books();
                    bk = book.get();
                    bk.setBookCondition(bookDetails.getBookCondition());
                    bk.setIsDeliverable(bookDetails.getIsDeliverable());
                    bk.setLendBookFor(bookDetails.getLendBookFor());
                    bk.setPincode(bookDetails.getPincode());
                    booksRepo.save(bk);
                }else{
                    throw new Error("Invalid book update request");
                }
            
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while updating book details",e);
            }

        }

        @Override
        public void deleteBook(int bookId) {
            try {
                Optional<Books> book = booksRepo.findByBookId(bookId);
                if(book.isPresent()){
                    Books bk = new Books();
                    bk = book.get();
                    bk.setActive(false);

                    booksRepo.save(bk);
                }else{
                    throw new Error("Invalid book request");
                }
                

            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while deleting book details",e);
            }
        }

        @Override
        public void requestBook(int bookId) {
            try {
                // get book details
                Optional<Books> book = booksRepo.findByBookId(bookId);
                if(book.isPresent()){

                    TransactionRequests request = new TransactionRequests();
                    request.setBookId(book.get().getBookId());
                    request.setRequestedBy(securityContext.getLoggedInUsername());
                    request.setStatus(0);
                    request.setLastUpdatedOn(new Date());
                    request.setRequestId(generatorService.getNextSequenceId("requests"));
                    // updating requests table
                    transactionReqRepo.save(request);

                     MailTemplates template =  emailService.getMailDetails(4);

                    Map<String, Object> mailVariables = new HashMap();
                    mailVariables.put("bookTitle", book.get().getTitle());
                    // sending notification mail
                    emailService.sendEmail(book.get().getUploadedBy(), template.getSubject(), template.getTemplatePath()
                    , mailVariables);
                
                }else{
                    // invalid book id
                    throw new Error("Invalid book request");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while requesting book",e);
            }
        }

        @Override
        public List<BookRequestsDTO> getRequestedBooks() {
            try {
                List<BookRequestsDTO> books = new ArrayList<>();

                List<TransactionRequests> requests = transactionReqRepo.getRequestedBooks(securityContext.getLoggedInUsername());

                for(int i=0;i<requests.size();i++){
                    Optional<Books> book = booksRepo.findByBookId(requests.get(i).getBookId());
                    if(book.isPresent()){
                        BookRequestsDTO request = new BookRequestsDTO();
                        int status = requests.get(i).getStatus();
                        switch(status) {
                            case 0:
                                request.setStatus("Pending");
                              break;
                            case 1:
                                request.setStatus("Accepted");
                              break;
                            case 2:
                                request.setStatus("Rejected");
                            break;
                            case 3:
                                request.setStatus("Cancelled");
                            break;
                              
                            default:
                            request.setStatus("Error getting status");
                          }
                          BeanUtils.copyProperties(requests.get(i), request);
                          BeanUtils.copyProperties(book.get(), request);
                          request.setLastUpdatedOn(requests.get(i).getLastUpdatedOn());
                          request.setRequestId(requests.get(i).getRequestId());
                          books.add(request);
                        }

                    
                }
                return books;

            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while retriving requested books");
            }
        }

        @Override
        public List<BookRequestsDTO> getBookRequests() {
           
            try {
                
                List<BookRequestsDTO> bookRequests = new ArrayList<>();
                // get books uploaded by user
                List<Books> books = booksRepo.findBooksByUploadedBy(securityContext.getLoggedInUsername());

                for(Books book:books){
                    Optional<TransactionRequests> request = transactionReqRepo.getPendingBookRequests(book.getBookId(),0);

                    if(request.isPresent()){
                        BookRequestsDTO bean = new BookRequestsDTO();
                        BeanUtils.copyProperties(request.get(), bean);
                        BeanUtils.copyProperties(book, bean);

                        Optional<Users> user = usersRepo.findByUsername(bean.getRequestedBy());
                        if(user.isPresent()){
                            bean.setRequestedBy(user.get().getFirstName()+" "+user.get().getMiddleName()+" "+user.get().getLastName());
                        }
                        switch (request.get().getStatus()) {
                            case 0:
                                bean.setStatus("Pending");
                                break;
                            case 1:
                                bean.setStatus("Pending");
                                break;
                            case 2:
                                bean.setStatus("Accepted");
                                break;
                        
                            default:
                                break;
                        }

                        bean.setYourRequest(true);

                        bookRequests.add(bean);
                    }

                }
                return bookRequests;


            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while retriving book requests");
            }
            
        }

        @Override
        public List<BookRequestsDTO> getAllTransactions() {
            
            try {
                List<BookRequestsDTO> allRequests = new ArrayList<>();

                allRequests.addAll(getBookRequests());
                allRequests.addAll(getRequestedBooks());

                return allRequests;

            } catch (Exception e) {
               e.printStackTrace();
               throw new Error("Error while retriving all transactions");
            }
            
        }

        @Override
        public void updateRequestStatus(int requestId, int status) {
            
            try {
                
                String toAddress ;
                MailTemplates template;
                Optional<TransactionRequests> request = transactionReqRepo.findByRequestId(requestId);

                if(request.isPresent()){
                    if(status==1 || status ==2 || status == 3){
                        request.get().setStatus(status);
                        transactionReqRepo.save(request.get());

                        if(status == 1 || status == 2){
                             template =  emailService.getMailDetails(status==1?5:6);
                             toAddress = request.get().getRequestedBy();
                        }else{
                             template =  emailService.getMailDetails(7);

                            Optional<Books> book =  booksRepo.findByBookId(request.get().getBookId());
                             toAddress = book.get().getUploadedBy();
                        }
                        
                    Map<String, Object> mailVariables = new HashMap();
                    // sending notification mail
                    emailService.sendEmail(toAddress, template.getSubject(), template.getTemplatePath()
                    , mailVariables);

                    }else{
                        throw new Error("Invalid status id");
                    }
                    
                }else{
                    throw new Error("Invalid request Id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while approving request");
            }

        }

        @Override
        public List<Integer> getBookCounts() {
           
            try {
                List<Integer> lst = new ArrayList<>();
                // uploaded books
                List<Books> uploads = booksRepo.findBooksByUploadedBy(securityContext.getLoggedInUsername());
                lst.add(uploads.size());
                // borrowed books
                List<TransactionRequests> borrows = transactionReqRepo.getBorrowedBooksCount(1,securityContext.getLoggedInUsername());
                lst.add(borrows.size());
                return lst;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error while getting book counts");
            }

        }

}
