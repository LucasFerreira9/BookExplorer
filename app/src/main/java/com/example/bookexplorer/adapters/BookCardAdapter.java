package com.example.bookexplorer.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookexplorer.R;
import com.example.bookexplorer.book.Book;
import com.example.bookexplorer.util.AsyncTask;
import com.example.bookexplorer.util.Image;

import java.util.ArrayList;
import java.util.List;

public class BookCardAdapter extends RecyclerView.Adapter<BookCardAdapter.ViewHolder>{
    private final List<Book> books = new ArrayList<>();
    private OnBookClickListener onBookClickListener = null;

    public interface OnBookClickListener{
        void onBookClick(View view,int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookName;
        private final TextView authorsNames;
        private final TextView bookDate;
        private final ImageView bookImage;
        public ViewHolder(View view) {
            super(view);

            bookName = view.findViewById(R.id.tv_book_name);
            authorsNames = view.findViewById(R.id.tv_book_authors);
            bookDate = view.findViewById(R.id.tv_book_date);
            bookImage = view.findViewById(R.id.img_book);
        }

        public TextView getBookName() {
            return bookName;
        }
        public TextView getAuthorsNames() {
            return authorsNames;
        }
        public TextView getBookYear() { return bookDate; }
        public ImageView getImageView(){ return bookImage; }

    }

    public BookCardAdapter() {
        super();
    }

    /**
     * Update the dataset of the Adapter
     *
     * @param books List<Books> containing the books data to populate views to be used
     * by RecyclerView
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateBooks(List<Book> books){
        this.books.clear();
        this.books.addAll(books);
        notifyDataSetChanged();
    }


    public Book getBookAtPosition(int pos){
        return books.get(pos);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bookcard, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Book book = books.get(position);

        String bookNameText = book.getTitle();
        String bookAuthorsText = Book.getAuthorsAsString(book.getAuthors());
        String bookDateText = book.getPublishedDate();

        viewHolder.getBookName().setText(bookNameText);
        viewHolder.getAuthorsNames().setText(bookAuthorsText);
        viewHolder.getBookYear().setText(bookDateText);

        if(onBookClickListener!=null)
            viewHolder.itemView.setOnClickListener(v -> onBookClickListener.onBookClick(v,position));

        fetchAndUpdateBookImage(viewHolder, book);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return books.size();
    }

    /**
     * fetch book image bitmap and update ImageView in the main thread
     *
     * @param viewHolder ViewHolder
     * @param book Book
     */
    private void fetchAndUpdateBookImage(@NonNull ViewHolder viewHolder, Book book) {
        new AsyncTask<>(
                () -> Image.fetchImageBitmap(book.getImageURL()),
                bitmap -> {
                    if (bitmap != null)
                        viewHolder.getImageView().setImageBitmap(bitmap);
                }
        ).run();
    }

    public void setOnBookClickListener(OnBookClickListener onBookClickListener){
        this.onBookClickListener = onBookClickListener;
    }

}
