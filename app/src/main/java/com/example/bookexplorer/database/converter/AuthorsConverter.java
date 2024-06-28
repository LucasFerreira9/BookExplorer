package com.example.bookexplorer.database.converter;

import androidx.room.TypeConverter;

import com.example.bookexplorer.book.Book;

import java.util.ArrayList;
import java.util.Arrays;

public class AuthorsConverter {
    @TypeConverter
    public ArrayList<String> toAuthorsList(String authors){
        if(authors==null) return null;

        return new ArrayList<>(Arrays.asList(authors.split(",")));

    }
    @TypeConverter
    public String toAuthorsString(ArrayList<String> authors){
        if(authors==null) return null;
        return Book.getAuthorsAsString(authors);
    }
}
