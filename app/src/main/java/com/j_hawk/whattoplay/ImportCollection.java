package com.j_hawk.whattoplay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;
import com.j_hawk.whattoplay.data.OnlineGame;
import com.j_hawk.whattoplay.services.ParserGame;
import com.j_hawk.whattoplay.services.SvcImportCollection;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

/**
 * Created by kevin on 10/30/2017.
 */

public class ImportCollection extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toast statusMessage;
    private EditText username;
    private Button importCollection;
    private TextView progressText;
    private ProgressBar progressBar;
    private KeyListener usernameListener;
    private Drawable editTextColor;
    private Drawable buttonColor;
    private Button cancelImport;
    private ArrayList<OnlineGame> onlineGames;
    private ImportGamesFromCollection importTask;
    private ArrayList<Integer> gamesAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_collection);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        gamesAdded = new ArrayList<>();
        username = (EditText)findViewById(R.id.bggUserNameEdTxt);
        importCollection = (Button)findViewById(R.id.importButton);
        cancelImport = (Button)findViewById(R.id.cancelImport);
        cancelImport.setVisibility(View.GONE);
        buttonColor = importCollection.getBackground();
        progressText = (TextView)findViewById(R.id.progressText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressText.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        usernameListener = username.getKeyListener();
        editTextColor = username.getBackground();
        setImportClickListener();

        cancelImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importTask.cancel(true);
                progressBar.setVisibility(View.GONE);
                progressText.setText("Import Cancelled");
                progressText.setVisibility(View.VISIBLE);
                username.setKeyListener(usernameListener);
                importCollection.setClickable(true);
                username.setText("");
                username.setBackground(editTextColor);
                importCollection.setText("Import Collection From BBG");
                importCollection.setBackground(buttonColor);
                cancelImport.setVisibility(View.GONE);
                for (int gameId : gamesAdded) {
                    Log.i("delete game", "" + dbHelper.removeGame(gameId));
                }

            }
        });
    }

    private void setImportClickListener() {
        importCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    importTask = new ImportGamesFromCollection();
                    onlineGames = new SvcImportCollection().execute(username.getText().toString()).get();
                    importTask.numberOfGames = onlineGames.size();
                    importTask.execute(onlineGames);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ImportCollection.this, HomePageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    private class ImportGamesFromCollection extends AsyncTask<ArrayList<OnlineGame>, Integer, Boolean> {
        protected int numberOfGames;
        protected String gameBeingImported;

        @Override
        protected Boolean doInBackground(ArrayList<OnlineGame>... arrayLists) {

            for (int i = 0; i < arrayLists[0].size(); i++) {
                if (isCancelled()) {
                    break;
                }

                OnlineGame onlineGame = arrayLists[0].get(i);
                publishProgress(i);
                String urlString= "https://www.boardgamegeek.com/xmlapi2/thing?id=" + onlineGame.getId();
                gameBeingImported = onlineGame.getName();
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    int retryCount = 1;
                    while (urlConnection.getResponseCode() != 200 && retryCount <= 4) {
                        urlConnection.disconnect();
                        Thread.sleep(1500 * retryCount);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        retryCount ++;
                    }
                    try {
                        InputStream in = urlConnection.getInputStream();
                        ParserGame parser = new ParserGame();
                        List<Game> game = parser.parse(in);
                        if (dbHelper.addGame(game.get(0)) != -1) {
                            gamesAdded.add(game.get(0).getId());
                        }

                    } finally {
                        urlConnection.disconnect();
                        Thread.sleep(750);
                    }

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
            if (!isCancelled()) {
                return  true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            gamesAdded.clear();
            progressBar.setMax(numberOfGames - 1);
            progressText.setText("We found " + numberOfGames + " in your collection");
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            username.setKeyListener(null);
            username.setBackgroundColor(Color.argb(10, 128,128,128));
            importCollection.setClickable(false);
            importCollection.setText("Importing games...");
            importCollection.setBackgroundColor(Color.argb(100, 102, 255, 102));
            cancelImport.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            progressText.setText("Finished Importing Collection\n\n" + gamesAdded.size() + " out of " + numberOfGames + " succesfully imported");
            progressText.setVisibility(View.VISIBLE);
            username.setKeyListener(usernameListener);
            importCollection.setClickable(true);
            username.setText("");
            username.setBackground(editTextColor);
            importCollection.setText("Import Collection From BBG");
            importCollection.setBackground(buttonColor);
            cancelImport.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressText.setText("Importing Game " + (values[0] + 1) + " of " + numberOfGames + "\n\n" + gameBeingImported);
            progressBar.setProgress(values[0]);

        }
    }
}
