package com.example.bookexplorer.viewmodel.BookVisualizerViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.repository.BookRepository;

public class BookVisualizerViewModel extends ViewModel {
    private final LiveData<Integer> saved;
    private final MutableLiveData<Boolean> isDescriptionExpanded = new MutableLiveData<>(false);
    private final BookRepository repository;
    private final Book book;

    public BookVisualizerViewModel(BookRepository repository, Book book){
        this.repository = repository;
        this.book = book;

        saved = repository.exists(book.getId());
    }
    public LiveData<Integer> getSavedLiveData() { return saved; }
    public LiveData<Boolean> getIsDescriptionExpanded(){ return isDescriptionExpanded; }
    public void expandDescription(){isDescriptionExpanded.setValue(true);}
    public void contractDescription(){isDescriptionExpanded.setValue(false);}
    public void updateSaved() {
        if(saved.getValue() != null && saved.getValue() == 1) repository.removeBook(book);
        else repository.saveBook(book);
    }
}
