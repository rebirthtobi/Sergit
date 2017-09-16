package com.rebirthstudio.sergit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private HashMap<String, Bitmap> gitterData = new HashMap<String, Bitmap>();
    private String gitterResponse;

    public GitterConnection(Context c){
        context = c;
    }

    public HashMap<String, Bitmap> getGitterData(){
        return gitterData;
    }

    public void createURL(String url){
        try{
            gitterURL = new URL(url);
        }
        catch (MalformedURLException exception){
            Toast.makeText(context, "Wrong URL", Toast.LENGTH_SHORT).show();
        }
    }

    public void connect() throws IOException{
        InputStream inputStream = null;
        HttpURLConnection gitterURLConnection = null;

        if(gitterURL == null){
            return;
        }

        if(!checkConnection()){
            ProgressBar spinner;
            spinner = (ProgressBar) context.findViewById(R.id.indeterminateBar);
        }

        try {
            gitterURLConnection = (HttpURLConnection) gitterURL.openConnection();
            gitterURLConnection.setRequestMethod("GET");
            gitterURLConnection.setReadTimeout(10000);
            gitterURLConnection.setConnectTimeout(15000);
            gitterURLConnection.connect();

            if(gitterURLConnection.getResponseCode() == 200) {
                inputStream = gitterURLConnection.getInputStream();
                read(inputStream);
            }
            else{
                Toast.makeText(context, "Not getting the right response", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException exception){
            Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
        }
        finally {
            if(gitterURLConnection != null){
                gitterURLConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    private void read(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        gitterResponse = output.toString();
        formatGitterData();
    }

    private void formatGitterData(){
        if(TextUtils.isEmpty(gitterResponse)){
            Toast.makeText(context, "Wrong data received", Toast.LENGTH_SHORT).show();
        }

        try {
            JSONObject gitterJSONObject = new JSONObject(gitterResponse);
            JSONArray gitterJSONArray = gitterJSONObject.getJSONArray("items");

            for(int i=0; i<gitterJSONArray.length(); i++){
                JSONObject gitterUserJSONObject = gitterJSONArray.getJSONObject(i);
                InputStream imageInputStream = getImageInputStream(gitterUserJSONObject.getString("avatar_url"));
                Bitmap bmp = BitmapFactory.decodeStream(imageInputStream);
                gitterData.put(gitterUserJSONObject.getString("login"), bmp);
            }
        }
        catch (JSONException e){
            Toast.makeText(context, "Error Parsing JSON data", Toast.LENGTH_SHORT).show();
        }
    }

    private InputStream getImageInputStream(String avatarUrl){
        URL imageUrl = null;
        InputStream imageInputStream = null;
        try {
            imageUrl = new URL(avatarUrl);
        } catch (MalformedURLException e) {
            Toast.makeText(context, "Wrong URL for avatar", Toast.LENGTH_SHORT).show();
        }
        try {
            imageInputStream = imageUrl.openConnection().getInputStream();
        } catch (IOException e) {
            Toast.makeText(context, "Error getting the picture stream", Toast.LENGTH_SHORT).show();
        }
        return imageInputStream;
    }

    private Boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }
}
