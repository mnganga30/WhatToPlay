package com.j_hawk.whattoplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private Toast statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        final Button button = (Button) findViewById(R.id.onlineBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Code here executes on main thread after user presses button

                startActivity(new  Intent(getApplicationContext(), AddToCollection.class));
            }
        });
    }

    public void goToRemoveGameActivity (View v) {
        Intent intent = new Intent(getApplicationContext(), removeGameActivity.class);
        startActivity(intent);
    }



}
