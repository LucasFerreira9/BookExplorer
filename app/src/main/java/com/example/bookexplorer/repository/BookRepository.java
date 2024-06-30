package com.example.bookexplorer.repository;

import androidx.lifecycle.LiveData;

import com.example.bookexplorer.BuildConfig;
import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.book.BookListDeserializer;
import com.example.bookexplorer.database.DAO.BookDAO;
import com.example.bookexplorer.resources.BookSearchResource;
import com.example.bookexplorer.util.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

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
        private AsyncTask<BookSearchResource> searchTask = null;
        final String CONNECTION_ERROR_MESSAGE = "Não foi possível se conectar a API, verifique sua conexão com a internet";

        public void search(String bookName,int timeBeforeSearch,Result<BookSearchResource> listener){
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

        private BookSearchResource fetchGoogleBooks(String bookName) throws Exception {
            String GOOGLE_API_KEY = BuildConfig.BOOKS_API_KEY;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(TYPE, new BookListDeserializer())
                    .setPrettyPrinting()
                    .create();

            String url = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(bookName,"UTF-8") + "&key=" + GOOGLE_API_KEY;

            try{
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();

                String bodyResponse = Objects.requireNonNull(body).string();
                List<Book> books = gson.fromJson(bodyResponse, new TypeToken<List<Book>>() {}.getType());
                return new BookSearchResource(books);
            }
            catch(IOException e){
                return new BookSearchResource(CONNECTION_ERROR_MESSAGE);
            }


        }
    }

}
