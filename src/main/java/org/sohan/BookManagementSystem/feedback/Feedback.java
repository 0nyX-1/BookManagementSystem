package org.sohan.BookManagementSystem.feedback;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sohan.BookManagementSystem.book.Book;
import org.sohan.BookManagementSystem.common.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Feedback extends BaseEntity {
    private Double note; // 1-5 stars
    private String comment; //comments


    @ManyToOne
    @JoinColumn(name = "book_id",referencedColumnName = "id")
    private Book book;


}
