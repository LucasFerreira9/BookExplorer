package com.example.bookexplorer.viewmodel.BooksViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.repository.BookRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<List<Book>> searchBooks = new MutableLiveData<>();
    private final LiveData<List<Book>> savedBooks;
    private final BookRepository.GoogleBooksFinder googleBooksFinder = new BookRepository.GoogleBooksFinder();

    public MainActivityViewModel(BookRepository repository) {
        savedBooks = repository.getSavedBooks();
    }
    public LiveData<List<Book>> getSavedBooks(){
        return savedBooks;
    }
    public LiveData<List<Book>> getSearchBooks(){
        return searchBooks;
    }
    public void updateSearchBooks(String bookName, int waitTime){
        googleBooksFinder.stopSearch();
        googleBooksFinder.search(bookName,waitTime,this.searchBooks::setValue);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        googleBooksFinder.stopSearch();
    }
}
