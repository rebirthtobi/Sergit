package com.rebirthstudio.sergit;

import android.graphics.Bitmap;

/**
 * Created by Tobi on 08-Sep-17.
 */

public class Gitter {
    private String username;
    private Bitmap useravatar;
    private String userurl;

    public Gitter(String username, Bitmap useravatar, String userurl){
        this.username = username;
        this.useravatar = useravatar;
        this.userurl = userurl;
    }

    public String getUsername(){
        return this.username;
    }

    public Bitmap getUseravatar(){
        return this.useravatar;
    }

    public String getUserurl(){
        return this.userurl;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setUseravatar (Bitmap useravatar){
        this.useravatar = useravatar;
    }

    public void setUserurl(String userurl){
        this.userurl = userurl;
    }
}
