package org.sohan.BookManagementSystem.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withOwnerId(Integer ownerId){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user").get("id"),ownerId);
        });
    }
}
