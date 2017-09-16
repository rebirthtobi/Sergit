package com.rebirthstudio.sergit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleGitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_gitter);
        Intent receiver = getIntent();
        Bitmap bitmap = (Bitmap) receiver.getParcelableExtra("useravatar");
        String username = receiver.getStringExtra("username");
        String userurl = receiver.getStringExtra("userurl");

        TextView usernameTextView = (TextView) findViewById(R.id.singleusername);
        usernameTextView.setText(username);

        TextView userurlTextView = (TextView) findViewById(R.id.singleuserurl);
        userurlTextView.setText(userurl);

        ImageView useravatarImageView = (ImageView) findViewById(R.id.singleuseravatar);
        useravatarImageView.setImageBitmap(bitmap);

        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo start a share intent
            }
        });
    }
}
