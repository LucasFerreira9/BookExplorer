package com.example.bookexplorer.viewmodel.BooksViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookexplorer.repository.BookRepository;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {
    private final BookRepository repository;

    public MainActivityViewModelFactory(BookRepository repository){
        this.repository = repository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(repository);
    }
}
