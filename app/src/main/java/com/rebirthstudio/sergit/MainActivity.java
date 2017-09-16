package com.rebirthstudio.sergit;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GitterPresenter gitterPresenter;
    private ArrayList<Gitter> gitters;
    private GitterAdapter gitterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gitter_list);
        gitters = new ArrayList<Gitter>();
        gitterPresenter = new GitterPresenter(this);
        gitterAdapter = new GitterAdapter(this, gitters);

        ListView listView = (ListView) findViewById(R.id.gitter_list);
        listView.setAdapter(gitterAdapter);

        new GitterAsynTask().execute();
    }

    private class GitterAsynTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            gitterPresenter.initialize();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gitters = gitterPresenter.getGitters();

            gitterAdapter.clear();
            gitterAdapter.addAll(gitters);
        }
    }
}
