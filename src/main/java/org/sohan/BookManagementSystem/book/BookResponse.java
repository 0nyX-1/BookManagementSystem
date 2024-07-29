package org.sohan.BookManagementSystem.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String user;
    private byte[] coverPhoto;
    private double rate;
    private boolean archived;
    private boolean shareable;


}
