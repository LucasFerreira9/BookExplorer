package com.example.bookexplorer.book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookListDeserializer implements JsonDeserializer<List<Book>> {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Book.class, new BookDeserializer())
            .create();

    @Override
    public List<Book> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Book> books = new ArrayList<>();
        JsonObject jsonObject = json.getAsJsonObject();
        int totalResults = Integer.parseInt(jsonObject.get("totalItems").getAsString());
        if(totalResults != 0){
            JsonArray items = jsonObject.getAsJsonArray("items");
            for(JsonElement item : items){
                Book book = gson.fromJson(item,Book.class);
                books.add(book);
            }
        }
        return books;
    }
}
