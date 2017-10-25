package com.j_hawk.whattoplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}
