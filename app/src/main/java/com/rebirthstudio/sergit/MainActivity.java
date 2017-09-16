package com.rebirthstudio.sergit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GitterPresenter gitterPresenter;
    private ArrayList<Gitter> gitters;
    private GitterAdapter gitterAdapter;
    private TextView emptyTextView;
    private ProgressBar spinner;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gitter_list);
        gitters = new ArrayList<Gitter>();
        gitterPresenter = new GitterPresenter(this);
        gitterAdapter = new GitterAdapter(this, gitters);
        emptyTextView = (TextView) findViewById(R.id.empty);
        spinner = (ProgressBar) findViewById(R.id.indeterminateBar);

        listView = (ListView) findViewById(R.id.gitter_list);
        listView.setAdapter(gitterAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gitter gitterUser = gitters.get(position);
                Intent singleGitter = new Intent(MainActivity.this, SingleGitter.class);
                singleGitter.putExtra("useravatar", gitterUser.getUseravatar());
                singleGitter.putExtra("username", gitterUser.getUsername());
                singleGitter.putExtra("userurl", gitterUser.getUserurl());
                startActivity(singleGitter);
            }
        });

        if(checkConnection()){
            listView.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            new GitterAsynTask().execute();
        }
        else{
            listView.setVisibility(View.GONE);
            emptyTextView.setText("Internet Connection not available");
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private class GitterAsynTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Boolean state = gitterPresenter.initialize();
            checkState(state);
            state = (state) ? gitterPresenter.processData():false;
            checkState(state);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gitters = gitterPresenter.getGitters();

            gitterAdapter.clear();
            gitterAdapter.addAll(gitters);
            spinner.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        private void checkState(Boolean state){
            if(!state){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.GONE);
                        emptyTextView.setText("Error occur in processing connection");
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    private Boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }
}
