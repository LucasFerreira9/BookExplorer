package com.example.bookexplorer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.database.DAO.BookDAO;
import com.example.bookexplorer.database.converter.AuthorsConverter;

@Database(entities = {Book.class}, version=1,exportSchema = false)
@TypeConverters({AuthorsConverter.class})
public abstract class BookExplorerDatabase extends RoomDatabase {
    private static final String DATABASE_FILE_NAME = "book_explorer.db";
    private static BookExplorerDatabase database = null;
    public abstract BookDAO getBookDAO();

    public static BookExplorerDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context,BookExplorerDatabase.class,DATABASE_FILE_NAME)
                    .build();
        }
        return database;
    }
}
