package com.rebirthstudio.sergit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tobi on 08-Sep-17.
 */

public class GitterAdapter extends ArrayAdapter<Gitter>{

    public GitterAdapter(Activity context, ArrayList<Gitter> gitters) {
        super(context, 0, gitters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Gitter gitter = getItem(position);

        TextView usernameTextView = (TextView) listItemView.findViewById(R.id.username);
        usernameTextView.setText(gitter.getUsername());

        ImageView useravatar = (ImageView) listItemView.findViewById(R.id.useravatar);
        useravatar.setImageBitmap(gitter.getUseravatar());

        return listItemView;
    }
}
