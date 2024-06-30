package com.example.bookexplorer.book;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BookDeserializer implements JsonDeserializer<Book> {
    private final Gson gson = new Gson();

    @Override
    public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject itemJsonObj = json.getAsJsonObject();
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

        return book;
    }
}
