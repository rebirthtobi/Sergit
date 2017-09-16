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
    private HashMap<String, HashMap<Bitmap, String>> gitterData = new HashMap<String, HashMap<Bitmap, String>>();
    private ArrayList<Gitter> gitters = new ArrayList<Gitter>();
    private Context context;
    private String githubURL = "https://api.github.com/search/users?q=location:nigeria";

    public GitterPresenter(Context c){
        context = c;
    };

    public Boolean initialize(){
        gitterConnection = new GitterConnection(context);
        if(gitterConnection.createURL(githubURL)) {
            try {
                if(gitterConnection.connect()){
                    gitterData = gitterConnection.getGitterData();
                    return true;
                }
                else{
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        else{
            return false;
        }
    }

    public ArrayList<Gitter> getGitters(){
        return gitters;
    }

    public Boolean processData(){
        if(gitterData.isEmpty()) {
            return false;
        }
        else{
            for(Map.Entry<String, HashMap<Bitmap, String>> pair: gitterData.entrySet()){
                String username = pair.getKey();
                HashMap.Entry<Bitmap, String> value = pair.getValue().entrySet().iterator().next();
                Bitmap avatar = value.getKey();
                String url = value.getValue();
                gitters.add(new Gitter(username, avatar, url));
            }
            return true;
        }
    }
}
