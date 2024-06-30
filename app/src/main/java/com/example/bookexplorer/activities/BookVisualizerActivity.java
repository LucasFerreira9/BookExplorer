package com.example.bookexplorer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookexplorer.R;
import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.database.BookExplorerDatabase;
import com.example.bookexplorer.repository.BookRepository;
import com.example.bookexplorer.viewmodel.BookVisualizerViewModel.BookVisualizerViewModel;
import com.example.bookexplorer.viewmodel.BookVisualizerViewModel.BookVisualizerViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BookVisualizerActivity extends AppCompatActivity {
    private Book book;
    private BookVisualizerViewModel bookVisualizerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_visualizer_activity);

        book = getBookExtra();
        addBookViewModel();
        setupViews();
        configureFabAddBook();
    }

    private void setupViews(){
        if(book != null){
            setupBookView();
            setupBookImageView();
            setupBookDescription();
            setupPDFDownloadBtn();
        }
    }

    private void setupBookView(){
        TextView tv_book_name_view = findViewById(R.id.tv_book_name);
        TextView tv_book_authors = findViewById(R.id.tv_book_authors);
        TextView tv_book_date = findViewById(R.id.tv_book_date);

        tv_book_name_view.setText(book.getTitle());
        tv_book_authors.setText(Book.getAuthorsAsString(book.getAuthors()));
        tv_book_date.setText(book.getPublishedDate());
    }

    private void setupBookImageView(){
        ImageView iv_book_image = findViewById(R.id.iv_book);
        Bitmap bookImage = getBookImageExtra();
        if(bookImage != null) iv_book_image.setImageBitmap(bookImage);
    }

    private void setupPDFDownloadBtn(){
        Button btn_downloadPdf = findViewById(R.id.btn_download_pdf);
        String pdfLink = book.getPdfLink();
        if(pdfLink == null){
            btn_downloadPdf.setEnabled(false);
            btn_downloadPdf.setText(String.format("%s (%s)",btn_downloadPdf.getText(),getString(R.string.unavailable)));
        }
        else
            btn_downloadPdf.setOnClickListener((v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pdfLink));
                startActivity(intent);
            }));
    }

    private void setupBookDescription(){
        TextView tv_expand_description = findViewById(R.id.tv_expand_description);
        TextView tv_book_description = findViewById(R.id.tv_book_description);
        tv_book_description.setText(book.getDescription());

        bookVisualizerViewModel.getIsDescriptionExpanded().observe(BookVisualizerActivity.this, (expanded)->{
            if(expanded){
                tv_expand_description.setOnClickListener(v -> bookVisualizerViewModel.contractDescription());
                tv_expand_description.setText(R.string.see_less);
                tv_book_description.setMaxLines(Integer.MAX_VALUE);
            }
            else{
                tv_expand_description.setOnClickListener(v -> bookVisualizerViewModel.expandDescription());
                tv_expand_description.setText(R.string.see_more);
                tv_book_description.setMaxLines(6);
            }
        });
    }

    private void configureFabAddBook(){
        FloatingActionButton fab_add_book = findViewById(R.id.fab_add_book);
        bookVisualizerViewModel.getSavedLiveData().observe(this,(saved)->{
            int imageResource =
                    saved == 1 ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_deselected;
            fab_add_book.setImageResource(imageResource);
        });
        fab_add_book.setOnClickListener((v -> bookVisualizerViewModel.updateSaved()));
    }

    private Bitmap getBookImageExtra() {
        byte[] byteArray = getIntent().getByteArrayExtra("book_image");
        if(byteArray != null){
            return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        }
        return null;
    }
    private Book getBookExtra(){
        return (Book)getIntent().getSerializableExtra("book");
    }

    private void addBookViewModel(){
        ViewModelProvider provider = new ViewModelProvider(this,new BookVisualizerViewModelFactory(
                new BookRepository(BookExplorerDatabase.getInstance(this).getBookDAO()),
                book));
        bookVisualizerViewModel = provider.get(BookVisualizerViewModel.class);
    }


}
