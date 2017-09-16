package com.rebirthstudio.sergit;

import android.graphics.Bitmap;

/**
 * Created by Tobi on 08-Sep-17.
 */

public class Gitter {
    private String username;
    private Bitmap useravatar;

    public Gitter(String username, Bitmap useravatar){
        this.username = username;
        this.useravatar = useravatar;
    }

    public String getUsername(){
        return this.username;
    }

    public Bitmap getUseravatar(){
        return this.useravatar;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setUseravatar (Bitmap useravatar){
        this.useravatar = useravatar;
    }
}
