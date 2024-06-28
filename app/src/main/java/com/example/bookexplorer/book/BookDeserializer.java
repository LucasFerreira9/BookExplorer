package com.example.bookexplorer.book;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookDeserializer implements JsonDeserializer<List<Book>> {
    private final Gson gson = new Gson();

    @Override
    public List<Book> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Book> books = new ArrayList<>();
        JsonObject jsonObject = json.getAsJsonObject();
        int totalResults = Integer.parseInt(jsonObject.get("totalItems").getAsString());
        if(totalResults != 0){
            JsonArray items = jsonObject.getAsJsonArray("items");
            for(JsonElement item : items){
                JsonObject itemJsonObj = item.getAsJsonObject();
                String id = itemJsonObj.get("id").getAsString();

                JsonObject volumeInfo = itemJsonObj.getAsJsonObject("volumeInfo");
                JsonObject accessInfo = itemJsonObj.getAsJsonObject("accessInfo");
                JsonObject saleInfo = itemJsonObj.getAsJsonObject("saleInfo");

                Book book = gson.fromJson(volumeInfo,Book.class);
                book.setId(id);
                book.setIsEbook(saleInfo.get("isEbook").getAsBoolean());

                if(saleInfo.get("saleability").getAsString().equals("FOR_SALE"))
                    book.setPrice(saleInfo.get("listPrice").getAsJsonObject().get("amount").getAsDouble());

                JsonObject pdfObj = accessInfo.get("pdf").getAsJsonObject();
                if(pdfObj.has("downloadLink") && pdfObj.get("isAvailable").getAsBoolean())
                    book.setPdfLink(pdfObj.get("downloadLink").getAsString());

                JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                if(imageLinks != null)
                    book.setImageUrl(imageLinks.get("thumbnail").getAsString());

                books.add(book);
            }
        }
        return books;
    }
}
