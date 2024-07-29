package org.sohan.BookManagementSystem.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sohan.BookManagementSystem.common.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/saveBook")
    public ResponseEntity<String> saveBook(
            @Valid @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ){
        System.out.println(connectedUser);
        return new ResponseEntity<>(bookService.saveBook(bookRequest,connectedUser), HttpStatus.OK);
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId
    )
    {
        return new ResponseEntity<BookResponse>(bookService.findBookById(bookId),HttpStatus.OK);
    }

    //Pagination Concept
    //Displayed all the books but not showing the books owned by user
    @GetMapping("/findAllBooks")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
             @RequestParam(name = "page",defaultValue = "0",required = false) int page,
             @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
             Authentication connectedUser
    ){
        return new ResponseEntity<PageResponse<BookResponse>>(
                bookService.findAllBooks(
                        page,pageSize,connectedUser
                ),
                HttpStatus.OK
        );
    }

    //Displaying all the books by owner
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
            Authentication connectedUser
    )
    {
        return new ResponseEntity<PageResponse<BookResponse>>(
                bookService.findAllBooksByOwner(
                        page,pageSize,connectedUser
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/findAllBorrowedBooks")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
            Authentication connectedUser
    ){

        return new ResponseEntity<PageResponse<BorrowedBookResponse>>(
                bookService.findAllBorrowedBooks(
                        page,pageSize,connectedUser
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/findReturnedBooks")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
            Authentication connectedUser
    ){

        return new ResponseEntity<PageResponse<BorrowedBookResponse>>(
                bookService.findALlReturnedBooks(
                        page,pageSize,connectedUser
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> shareableStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return
                new ResponseEntity<Integer>(
                        bookService.updateShareableStatus(
                                bookId,connectedUser
                        ),
                        HttpStatus.OK
                );
    }
    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return
                new ResponseEntity<Integer>(
                        bookService.updateArchivedStatus(
                                bookId,connectedUser
                        ),
                        HttpStatus.OK
                );
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return
                new ResponseEntity<>(
                        bookService.borrowBook(
                                bookId, connectedUser
                        ),
                        HttpStatus.OK
                );
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(

            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    )
    {
        return new ResponseEntity<>(
                bookService.returnBorrowedBook(bookId,connectedUser),
                HttpStatus.OK
        );
    }

    @PatchMapping("/borrow/return-approved/{book-id}")
    public ResponseEntity<Integer> approveReturnedBooks(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    )
    {
        return new ResponseEntity<>(
                bookService.approveReturnedBooks(bookId,connectedUser),
                HttpStatus.OK
        );
    }
}

