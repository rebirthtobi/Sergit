package com.rebirthstudio.sergit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by Tobi on 08-Sep-17.
 */

public class GitterConnection {

    private Context context;
    private URL gitterURL;
    private HashMap<String, HashMap<Bitmap, String>> gitterData = new HashMap<String, HashMap<Bitmap, String>>();
    private String gitterResponse;

    public GitterConnection(Context c){
        context = c;
    }

    public HashMap<String, HashMap<Bitmap, String>> getGitterData(){
        return gitterData;
    }

    public Boolean createURL(String url){
        try{
            gitterURL = new URL(url);
            return true;
        }
        catch (MalformedURLException exception){
            return false;
        }
    }

    public Boolean connect() throws IOException{
        InputStream inputStream = null;
        HttpURLConnection gitterURLConnection = null;

        if(gitterURL == null){
            return false;
        }

        try {
            gitterURLConnection = (HttpURLConnection) gitterURL.openConnection();
            gitterURLConnection.setRequestMethod("GET");
            gitterURLConnection.setReadTimeout(10000);
            gitterURLConnection.setConnectTimeout(15000);
            gitterURLConnection.connect();

            if(gitterURLConnection.getResponseCode() == 200) {
                inputStream = gitterURLConnection.getInputStream();
                return read(inputStream);
            }
            else{
                return false;
            }
        }
        catch (IOException exception){
            return false;
        }
        finally {
            if(gitterURLConnection != null){
                gitterURLConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
            return true;
        }
    }

    private Boolean read(InputStream inputStream) throws IOException {


        if(inputStream != null){
            StringBuilder output = new StringBuilder();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }

            gitterResponse = output.toString();
            return formatGitterData();
        }else{
            return false;
        }


    }

    private Boolean formatGitterData(){
        if(TextUtils.isEmpty(gitterResponse)){
            return false;
        }

        try {
            JSONObject gitterJSONObject = new JSONObject(gitterResponse);
            JSONArray gitterJSONArray = gitterJSONObject.getJSONArray("items");

            for(int i=0; i<gitterJSONArray.length(); i++){
                JSONObject gitterUserJSONObject = gitterJSONArray.getJSONObject(i);
                InputStream imageInputStream = getImageInputStream(gitterUserJSONObject.getString("avatar_url"));
                Bitmap bmp = BitmapFactory.decodeStream(imageInputStream);
                HashMap<Bitmap, String> map = new HashMap<Bitmap, String>();
                map.put(bmp, gitterUserJSONObject.getString("html_url"));
                gitterData.put(gitterUserJSONObject.getString("login"), map);
            }

            return true;
        }
        catch (JSONException e){
            return false;
        }
    }

    private InputStream getImageInputStream(String avatarUrl){
        URL imageUrl = null;
        InputStream imageInputStream = null;
        try {
            imageUrl = new URL(avatarUrl);
        } catch (MalformedURLException e) {

        }
        try {
            imageInputStream = imageUrl.openConnection().getInputStream();
        } catch (IOException e) {

        }
        return imageInputStream;
    }

}
