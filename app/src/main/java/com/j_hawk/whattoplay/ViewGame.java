package com.j_hawk.whattoplay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j_hawk.whattoplay.R;
import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by martin on 11/17/2017.
 */

public class ViewGame extends AppCompatActivity {


        private DBHelper dbHelper;
        private Toast statusMessage;
        private ListView gameList;

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgameinfor);
            int id = getIntent().getIntExtra("Id",0);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);
           final TextView name = (TextView) findViewById(R.id.searchedName);
            final TextView year =  (TextView)findViewById(R.id.searchedYear);
            final TextView description= (TextView) findViewById(R.id.searchedDescription);
            final TextView age = (TextView) findViewById(R.id.searchedAge);
            final TextView numPlayers = (TextView) findViewById(R.id.searchedNumOfPlayers);
            final TextView playTime = (TextView) findViewById(R.id.searchedPlayTime);
            ImageView image = (ImageView) findViewById(R.id.searchedImage);

            Game selected = dbHelper.getGameByID(id);

            name.setText(selected.getName());

            if (selected.getYear() != 0000) {
                year.setText("[" + Integer.toString(selected.getYear()) + "]");
            }else {year.setText("");}

            description.setText(selected.getDescription());

            if (selected.getMinPlayerAge() != 0) {
                age.setText("Recommended Age: " + selected.getMinPlayerAge());
            }else{age.setText("Recommend Age: NAN");}

            if (selected.getPlayTime() != 0) {
                playTime.setText("Play Time: " + selected.getPlayTime());
            }else{ playTime.setText("Play Time: NaN");}

            numPlayers.setText("Number of Players " + selected.getMinPlayers()  + " - "+ selected.getMaxPlayers());

            try {
                image.setImageBitmap(new DownloadImage().execute(selected.getThumbnail()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }




    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //image.setImageBitmap(result);
        }
    }

    }
