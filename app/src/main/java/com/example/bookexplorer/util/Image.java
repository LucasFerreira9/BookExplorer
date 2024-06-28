package com.example.bookexplorer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Image {

    /**
     * fetch image by url and return the respective bitmap object
     *
     * @param urlString String
     * @return bitmap Bitmap
     */
    public static Bitmap fetchImageBitmap(String urlString){
        Bitmap bitmap = null;

        try{
            urlString = urlString.replace("http","https");
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(input);
        }
        catch(Exception ignored){/*cannot find image*/}

        return bitmap;
    }

    /**
     * converts image bitmap to ByteArray.
     *
     * @param bitmap Bitmap
     * @return bytearray byte[]
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
