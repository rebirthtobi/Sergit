package com.rebirthstudio.sergit;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobi on 08-Sep-17.
 */

public class GitterPresenter {
    private GitterConnection gitterConnection;
    private HashMap<String, Bitmap> gitterData = new HashMap<String, Bitmap>();
    private ArrayList<Gitter> gitters = new ArrayList<Gitter>();
    private Context context;
    private String githubURL = "https://api.github.com/search/users?q=location:nigeria";

    public GitterPresenter(Context c){
        context = c;
    };

    public void initialize(){
        gitterConnection = new GitterConnection(context);
        gitterConnection.createURL(githubURL);
        try {
            gitterConnection.connect();
        } catch (IOException e) {
            Toast.makeText(context, "Unable to connect", Toast.LENGTH_LONG).show();
        }

        gitterData = gitterConnection.getGitterData();

        processData();
    }

    public ArrayList<Gitter> getGitters(){
        return gitters;
    }

    private void processData(){
        if(gitterData.isEmpty()) {
            Toast.makeText(context, "No Gitters Found", Toast.LENGTH_LONG).show();
        }
        else{
            for(Map.Entry<String, Bitmap> pair: gitterData.entrySet()){
                gitters.add(new Gitter(pair.getKey(), pair.getValue()));
            }
        }
    }
}
