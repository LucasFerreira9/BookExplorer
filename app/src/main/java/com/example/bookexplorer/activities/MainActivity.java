package com.example.bookexplorer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookexplorer.R;
import com.example.bookexplorer.adapters.BookCardAdapter;
import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.database.BookExplorerDatabase;
import com.example.bookexplorer.repository.BookRepository;
import com.example.bookexplorer.util.Image;
import com.example.bookexplorer.viewmodel.MainActivityViewModel.MainActivityViewModel;
import com.example.bookexplorer.viewmodel.MainActivityViewModel.MainActivityViewModelFactory;
import com.google.android.material.search.SearchView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private BookCardAdapter resultListAdapter;
    private BookCardAdapter savedListAdapter;
    private MainActivityViewModel mainActivityViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        createBooksViewModel();
        addSavedBooksObserver();
        addSearchBooksObserver();

        configureAdapters();
        addTextChangeListener();
    }

    private void createBooksViewModel(){
        BookRepository bookRepository = new BookRepository(BookExplorerDatabase.getInstance(this).getBookDAO());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this,new MainActivityViewModelFactory(bookRepository));
        mainActivityViewModel = viewModelProvider.get(MainActivityViewModel.class);
    }

    private void addSavedBooksObserver(){
        mainActivityViewModel.getSavedBooks().observe(this,(savedBooks)->{
            if(savedBooks!=null) savedListAdapter.updateBooks(savedBooks);
        });
    }

    private void addSearchBooksObserver(){
        mainActivityViewModel.getSearchBooks().observe(this,(bookSearchResource)->{
            if(bookSearchResource!=null) {
                List<Book> result = bookSearchResource.getResult();
                if(result != null)
                    resultListAdapter.updateBooks(result);
                else
                    showErrorToast(bookSearchResource.getErrorMessage());
            };
        });
    }

    private void showErrorToast(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void configureAdapters(){
        RecyclerView resultList = findViewById(R.id.result_list);
        RecyclerView savedList = findViewById(R.id.saved_books_list);

        resultListAdapter = new BookCardAdapter();
        savedListAdapter = new BookCardAdapter();

        resultListAdapter.setOnBookClickListener(getOnBookClickListener(resultListAdapter));
        savedListAdapter.setOnBookClickListener(getOnBookClickListener(savedListAdapter));

        savedList.setAdapter(savedListAdapter);
        resultList.setAdapter(resultListAdapter);
    }

    private BookCardAdapter.OnBookClickListener getOnBookClickListener(BookCardAdapter adapter){
        return (view, position) -> {
            Book bookAtPosition = adapter.getBookAtPosition(position);

            ImageView iv_book = view.findViewById(R.id.img_book);

            Bitmap bookImage = null;
            Drawable bookDrawable = iv_book.getDrawable();
            if(bookDrawable instanceof BitmapDrawable)
                bookImage = ((BitmapDrawable)bookDrawable).getBitmap();

            goToBookVisualizerActivity(bookAtPosition,bookImage);
        };
    }

    private void addTextChangeListener(){
        SearchView searchView = findViewById(R.id.search_view);
        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mainActivityViewModel.updateSearchBooks(s.toString(),1300);
            }
        });
    }

    private void goToBookVisualizerActivity(Book book, Bitmap bookImage){
        Intent intent = new Intent(this, BookVisualizerActivity.class);
        intent.putExtra("book",book);
        if(bookImage != null) intent.putExtra("book_image",Image.bitmapToByteArray(bookImage));
        startActivity(intent);
    }

}
