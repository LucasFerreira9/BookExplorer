package com.example.bookexplorer.resources;

import com.example.bookexplorer.book.Book;

import java.util.List;

public class BookSearchResource {
    private final String errorMessage;
    private final List<Book> result;

    public BookSearchResource(String errorMessage){
        result = null;
        this.errorMessage = errorMessage;
    }
    public BookSearchResource(List<Book> result){
        this.result = result;
        errorMessage = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Book> getResult() {
        return result;
    }
}
