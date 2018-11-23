package com.example.anna.mymahjong.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by anna on 25.05.17.
 */

public class RulesActivity extends AppCompatActivity {

    Button backToMenuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);



        backToMenuButton =(Button)findViewById(R.id.playAgainButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulesActivity.this, MainActivity.class);
                startActivity(intent);            }
        });
    }
}
