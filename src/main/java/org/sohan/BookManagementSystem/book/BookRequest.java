package org.sohan.BookManagementSystem.book;

//Book Request will help to creating and updating the book

public record BookRequest(
        Integer id,

//        @NotNull(message="100") // It is used to sent data to frontend. Like lets say "100 Title should not be empty"
//        @NotEmpty(message = "100")
        String title,

//        @NotNull(message="100")
//        @NotEmpty(message = "100")
        String author,

//        @NotNull(message="100")
//        @NotEmpty(message = "100")
        String isbn,

//        @NotNull(message="100")
//        @NotEmpty(message = "100")
        String synopsis,

        boolean shareable

) {
}
