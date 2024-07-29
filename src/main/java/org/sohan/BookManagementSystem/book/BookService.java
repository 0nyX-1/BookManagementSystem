package org.sohan.BookManagementSystem.book;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sohan.BookManagementSystem.Exception.OperationNotPermittedOperation;
import org.sohan.BookManagementSystem.common.PageResponse;
import org.sohan.BookManagementSystem.history.BookTransactionHistory;
import org.sohan.BookManagementSystem.history.BookTransactionHistoryRepo;
import org.sohan.BookManagementSystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepo bookTransactionHistoryRepo;

    public String saveBook(BookRequest bookRequest, Authentication connectedUser) {
        System.out.println(connectedUser.getPrincipal());
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setUser(user);
        System.out.println("This is user "+user);
        return bookRepository.save(book).getTitle();
    }

    public BookResponse findBookById(Integer id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookResponse)

                //You can use lambda expression as well.
//                .map(book-> bookMapper.toBookResponse(book))

                .orElseThrow(() -> new EntityNotFoundException("No book with " + id + " id was found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int pageSize, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Book> books = bookRepository.findAllDisplayableBook(pageable, user.getId());
        System.out.println("The books are "+books);

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        System.out.println(bookResponses);

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(
            int page, int pageSize, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Book> booksByOwner = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> booksByOwnerList = booksByOwner.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                booksByOwnerList,
                booksByOwner.getNumber(),
                booksByOwner.getSize(),
                booksByOwner.getTotalElements(),
                booksByOwner.isFirst(),
                booksByOwner.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(
            int page, int pageSize, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // Relation between book and book transition history

        Page<BookTransactionHistory> allBorrowedBooks
                = bookTransactionHistoryRepo.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> borrowedBookResponses =
                allBorrowedBooks.stream()
                        .map(bookMapper::toBorrowedBook)
                        .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findALlReturnedBooks(int page, int pageSize, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // Relation between book and book transition history
        Page<BookTransactionHistory> allBorrowedBooks
                = bookTransactionHistoryRepo.findAllReturnedBooks(pageable, user.getId());

        List<BorrowedBookResponse> borrowedBookResponses =
                allBorrowedBooks.stream()
                        .map(bookMapper::toBorrowedBook)
                        .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with that ID wasn't found")
                );
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getUser().getId(), user.getId())) {
            throw new OperationNotPermittedOperation
                    ("You can't update the shareable status of the book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);

        return book.getId();
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with that ID wasn't found")
                );
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getUser().getId(), user.getId())) {
            throw new OperationNotPermittedOperation
                    ("You can't update the archived status of the book");
        }
        book.isArchived(!book.isArchived());
        bookRepository.save(book);

        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException(
                            "Book Not found with the given id"
                    );
                });

        //if book is not achievable or not shareable then it is
        //operation not permitted operation
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedOperation(
                    "The requested book is not shareable"
            );
        }
        User user = (User) connectedUser.getPrincipal();

        // Checking if book is with user or not
        // as user can't borrow his own book
        if (Objects.equals(book./*This one is owner */getUser().getId(),
                /*This one is user*/
                user.getId())) {
            throw new OperationNotPermittedOperation("You have that book. Can't borrow same book");
        }

        final boolean isAlreadyBorrowed = bookTransactionHistoryRepo
                .isAlreadyBorrowedByUser(bookId, user.getId());

        //Checking if the other user has borrowed book or not
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedOperation(
                    "This book is already borrowed"
            );
        }
        BookTransactionHistory bookTransactionHistory
                = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false) // when book is borrowed returned is false.
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepo.save(bookTransactionHistory)
                .getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException(
                            "Book Not found with the given id"
                    );
                });

        //if book is not achievable or not shareable then it is
        //operation not permitted operation
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedOperation(
                    "The requested book is not shareable"
            );
        }

        User user = (User) connectedUser.getPrincipal();

        // Checking if book is with user or not
        // as user can't borrow his own book
        if (Objects.equals(book.getUser().getId(), user.getId())) {
            throw new OperationNotPermittedOperation("You have that book. Can't return same book");
        }
        BookTransactionHistory bookTransactionHistory
                    = bookTransactionHistoryRepo.findByBookIdAndUserId(bookId,user.getId())
                .orElseThrow(()-> new OperationNotPermittedOperation("You didn't borrowed the book"));

        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnedBooks(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException(
                            "Book Not found with the given id"
                    );
                });

        //if book is not achievable or not shareable then it is
        //operation not permitted operation
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedOperation(
                    "The requested book is not shareable"
            );
        }

        User user = (User) connectedUser.getPrincipal();

        // Checking if book is with user or not
        // as user can't borrow his own book
        if (Objects.equals(book.getUser().getId(), user.getId())) {
            throw new OperationNotPermittedOperation("You have that book. Can't return same book");
        }
        BookTransactionHistory bookTransactionHistory
                = bookTransactionHistoryRepo.findByBookIdAndUserIdForReturnedOnes(bookId,user.getId())
                .orElseThrow(()-> new OperationNotPermittedOperation("Book is Not returned yet"));

        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }
}
