package org.sohan.BookManagementSystem.book;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.sohan.BookManagementSystem.common.BaseEntity;
import org.sohan.BookManagementSystem.feedback.Feedback;
import org.sohan.BookManagementSystem.history.BookTransactionHistory;
import org.sohan.BookManagementSystem.user.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseEntity {
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private boolean archived;
    private boolean shareable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="users_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private Set<BookTransactionHistory> bookTransactionHistorySet;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Feedback> feedbackList;

    public double getRate(){
       if (feedbackList==null || feedbackList.isEmpty()) return 0.0;

       else{
           double rate = feedbackList.stream()
                   .mapToDouble(Feedback::getNote)
                   .average()
                   .orElse(0.0);
           return Math.round(rate);
       }
    }
    public void isArchived(boolean archived){
        this.archived = archived;
    }
}
