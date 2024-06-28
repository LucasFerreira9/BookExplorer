package com.example.bookexplorer.repository;

import androidx.lifecycle.LiveData;

import com.example.bookexplorer.BuildConfig;
import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.book.BookDeserializer;
import com.example.bookexplorer.database.DAO.BookDAO;
import com.example.bookexplorer.util.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BookRepository {
    private final LiveData<List<Book>> savedBooks;
    private final BookDAO bookDAO;
    public interface Result<T> {
        void onResult(T result);
    }

    public BookRepository(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        savedBooks = bookDAO.getAll();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }

    public void saveBook(Book book) {
        AsyncTask.runSingleTask(() -> bookDAO.saveBook(book));
    }

    public void removeBook(Book book) {
        AsyncTask.runSingleTask(() -> bookDAO.removeBook(book));
    }

    public LiveData<Integer> exists(String id){
        return bookDAO.exists(id);
    }

    public static class GoogleBooksFinder{
        private final Type TYPE = new TypeToken<List<Book>>() {}.getType();
        private AsyncTask<List<Book>> searchTask = null;

        public void search(String bookName,int timeBeforeSearch,Result<List<Book>> listener){
            if(bookName.isEmpty()) return;

            searchTask = new AsyncTask<>(
                    ()->{
                        Thread.sleep(timeBeforeSearch);
                        return fetchGoogleBooks(bookName);
                    },
                    listener::onResult
            );
            searchTask.run();
        }
        public void stopSearch(){
            if(searchTask!=null) searchTask.cancel();
        }

        private List<Book> fetchGoogleBooks(String bookName) throws Exception {
            String GOOGLE_API_KEY = BuildConfig.BOOKS_API_KEY;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(TYPE, new BookDeserializer())
                    .setPrettyPrinting()
                    .create();

            String url = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(bookName,"UTF-8") + "&key=" + GOOGLE_API_KEY;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            if (body == null) throw new Exception();
            String bodyResponse = body.string();
            return gson.fromJson(bodyResponse, new TypeToken<List<Book>>() {}.getType());
        }
    }

}
