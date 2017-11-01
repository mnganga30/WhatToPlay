package com.j_hawk.whattoplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.OnlineGame;
import com.j_hawk.whattoplay.services.SvcImportCollection;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by kevin on 10/30/2017.
 */

public class ImportCollection extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toast statusMessage;
    private EditText username;
    private Button importCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_collection);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        username = (EditText)findViewById(R.id.bggUserNameEdTxt);
        importCollection = (Button)findViewById(R.id.importButton);

        importCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ArrayList<OnlineGame> onlineGames = new SvcImportCollection().execute(username.getText().toString()).get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
