package com.j_hawk.whattoplay;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.j_hawk.whattoplay.services.FindGameByID;
import com.j_hawk.whattoplay.services.FindGamesByQuery;
import com.j_hawk.whattoplay.data.OnlineGame;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by martin on 10/27/2017.
 */

public class AddToCollection extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toast statusMessage;
    private ListView gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_search);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);
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
        Log.i("test", newGame.toString());
        long result = dbHelper.addGame(newGame);
        if (result != -1) {
            statusMessage.setText("Game Succesfully Added To Collection!");
        } else {
            statusMessage.setText("ERROR: Game is already in your collection");
        }
        statusMessage.show();
    }
}
