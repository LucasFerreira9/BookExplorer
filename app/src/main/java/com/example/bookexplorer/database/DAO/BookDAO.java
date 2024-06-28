package com.example.bookexplorer.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bookexplorer.book.Book;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void saveBook(Book book);

    @Delete
    void removeBook(Book book);

    @Query("SELECT * FROM book")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM book WHERE id==:id")
    LiveData<Book> getSavedBookByID(String id);

    @Query("SELECT EXISTS(SELECT 1 FROM book WHERE id==:id)")
    LiveData<Integer> exists(String id);
}
