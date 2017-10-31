package com.j_hawk.whattoplay;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;
import com.j_hawk.whattoplay.services.OnlineGame;
import com.j_hawk.whattoplay.services.ParserGame;
import com.j_hawk.whattoplay.services.ParserGameList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by martin on 10/27/2017.
 */

public class AddToCollection extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toast statusMessage;
    private ProgressBar progressBar;
    private ListView gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_search);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        gameList = (ListView)findViewById(R.id.listView);
    }

    public void searchButtonClicked(View v) throws ExecutionException, InterruptedException {
        final TextView findGameText = (TextView)findViewById(R.id.removeGame);
        final ArrayList<OnlineGame> gameResults = new FindGamesByQuery().execute(findGameText.getText().toString()).get();
        if (gameResults == null) {
            statusMessage.setText("ERROR: No Games were returned for that search result.");
            statusMessage.show();
        } else {
            final ArrayAdapter adapterTask = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gameResults);
            gameList.setAdapter(adapterTask);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int index = i;
                    dialog.setTitle("Add "+ gameResults.get(i).getName()+" To Collection")
                            .setMessage("Would you like to add " + gameResults.get(i).getName() + " to your collection?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        addGameToCollection(gameResults.get(index).getId());
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    //.setIcon(android.R.drawable.ic_dialog_alert);
                    final AlertDialog alertDialog = dialog.show();
                    if (!alertDialog.isShowing()) alertDialog.dismiss();
                }
            });
        }
    }

    public void addGameToCollection(int id) throws ExecutionException, InterruptedException {
        Game newGame = new FindGameByID().execute(id).get();
        long result = dbHelper.addGame(newGame);
        if (result != -1) {
            statusMessage.setText("Game Succesfully Added To Collection!");
        } else {
            statusMessage.setText("ERROR: Game is already in your collection");
        }
        statusMessage.show();
    }

    private class FindGamesByQuery extends AsyncTask<String, Void, ArrayList<OnlineGame>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<OnlineGame> doInBackground(String... params) {
            ArrayList<OnlineGame> gameResults;
            String urlString= "https://www.boardgamegeek.com/xmlapi2/search?type=boardgame&query=" + params[0];
            Log.i("test", urlString);
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("test", "" + urlConnection.getResponseCode());
                try {
                    InputStream in = urlConnection.getInputStream();
                    ParserGameList parser = new ParserGameList();
                    List<OnlineGame> gameList = parser.parse(in);
                    gameResults = new ArrayList<>(gameList);
                    return gameResults;
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<OnlineGame> onlineGames) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(onlineGames);
        }
    }

    private class FindGameByID extends AsyncTask <Integer, Void, Game> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
            super.onPreExecute();
        }

        @Override
        protected Game doInBackground(Integer... params) {
            String urlString= "https://www.boardgamegeek.com/xmlapi2/thing?id=" + params[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    ParserGame parser = new ParserGame();
                    List<Game> game = parser.parse(in);
                    return game.get(0);

                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Game game) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(game);
        }
    }
}
