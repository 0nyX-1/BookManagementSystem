package org.sohan.BookManagementSystem.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Integer>,
        JpaSpecificationExecutor<Book> {

    /*Manual custom query */
    @Query(
            """
            select book
            from Book book
            where book.archived = false
            AND book.shareable = true
            and book.user.id!= :userId
            """
    )
    Page<Book> findAllDisplayableBook(Pageable pageable, @Param("userId") Integer userId);
}
