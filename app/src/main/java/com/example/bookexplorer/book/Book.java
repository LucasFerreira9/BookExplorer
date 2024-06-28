package com.example.bookexplorer.book;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private final String title;
    private final ArrayList<String> authors;
    private final String publishedDate;
    private final String description;
    private String imageURL = null;
    private boolean eBook;
    private double price = 0;
    private String pdfLink = null;

    public Book(@NonNull String id, String title, ArrayList<String> authors, String publishedDate, String description) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.description = description;
    }

    public void setPrice(double price){ this.price = price;}
    public double getPrice(){ return price; }

    public void setIsEbook(boolean isEbook){this.eBook = isEbook;}
    public boolean isEbook(){ return eBook; }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean getEBook() {
        return eBook;
    }

    public void setEBook(boolean eBook) {
        this.eBook = eBook;
    }

    public String getPdfLink() {
        return pdfLink;
    }
    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public void setImageUrl(String imageURL){
        this.imageURL = imageURL;
    }

    public void setId(@NonNull String id) {this.id = id;}

    @NonNull
    public String getId(){ return id; }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }
    public static String getAuthorsAsString(List<String> authors){
        if(authors==null) return "";

        StringBuilder builder = new StringBuilder();
        for(String author : authors){
            builder.append(author);
            builder.append(", ");
        }

        builder.deleteCharAt(builder.length()-2);

        return builder.toString();
    }
    public String getPublishedDate() { return publishedDate; }

    @NonNull
    @Override
    public String toString() {
        return "book: " + title + "\n";
    }

}
