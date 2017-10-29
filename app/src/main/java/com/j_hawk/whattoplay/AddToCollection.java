package com.j_hawk.whattoplay;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;

/**
 * Created by martin on 10/27/2017.
 */

public class AddToCollection extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toast statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_search);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        final Button button = (Button) findViewById(R.id.searchBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Code here executes on main thread after user presses button

            }
        });

    }

}
