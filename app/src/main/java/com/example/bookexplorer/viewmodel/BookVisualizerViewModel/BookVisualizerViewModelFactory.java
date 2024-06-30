package com.example.bookexplorer.viewmodel.BookVisualizerViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.repository.BookRepository;

public class BookVisualizerViewModelFactory implements ViewModelProvider.Factory {
    private final BookRepository repository;
    private final Book book;

    public BookVisualizerViewModelFactory(BookRepository repository, Book book){
        this.repository = repository;
        this.book = book;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BookVisualizerViewModel(repository, book);
    }
}
