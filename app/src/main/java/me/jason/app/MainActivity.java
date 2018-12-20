package me.jason.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.jason.app.ui.FirstActivity;
import me.jason.app.ui.SecondActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Base On Activity
        findViewById(R.id.buttonActivity).setOnClickListener(view -> {
            FirstActivity.start(this);
        });
        // Base On Fragment
        findViewById(R.id.buttonFragment).setOnClickListener(view -> {
            SecondActivity.start(this);
        });
        // Base On View
        findViewById(R.id.buttonView).setOnClickListener(view -> {
            SecondActivity.start(this);
        });
    }
}
