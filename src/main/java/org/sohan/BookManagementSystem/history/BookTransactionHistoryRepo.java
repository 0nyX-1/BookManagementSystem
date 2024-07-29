package org.sohan.BookManagementSystem.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookTransactionHistoryRepo extends JpaRepository<BookTransactionHistory,Integer> {

    @Query( """
            select hist
            from BookTransactionHistory hist
            WHERE hist.user.id = :userId
            """
    )
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable,
                                                      @Param("userId") Integer userId);

    /* When user returns the book he also can check all the returned book */
    @Query( """
            select hist
            from BookTransactionHistory hist
            WHERE hist.book.user.id = :userId
            """
    )
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);


    /*   in order
         checking whether book has been borrowed by others or not
         checking whether the connected user has borrowed book or not
    */
    @Query("""
            select
            (count(*) > 0 ) As isBorrowed
            from BookTransactionHistory bookTransactionHistory
            where bookTransactionHistory.book.id = :bookId
            and bookTransactionHistory.user.id = :userId
            and bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(
            @Param("bookId") Integer bookId,
            @Param("userId")Integer userId);

    @Query(
            """
            Select transaction
            from BookTransactionHistory transaction
            where transaction.user.id = :userId
            and transaction.book.id = :bookId
            and transaction.returned = false
            and transaction.returnApproved = false
            """
    )
    Optional<BookTransactionHistory> findByBookIdAndUserId(
            @Param("bookId") Integer bookId,
            @Param("userId") Integer userId);


    @Query(
            """
            Select transaction
            from BookTransactionHistory transaction
            where transaction.book.user.id = :userId
            and transaction.book.id = :bookId
            and transaction.returned = true
            and transaction.returnApproved = false
            """
    )
    Optional<BookTransactionHistory> findByBookIdAndUserIdForReturnedOnes(
            @Param("bookId") Integer bookId,
            @Param("userId") Integer userId);
}
