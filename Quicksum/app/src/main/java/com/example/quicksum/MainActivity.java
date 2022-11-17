package com.example.quicksum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        Button button = (Button) view;

        int number = Integer.parseInt(button.getText().toString());
        sum += number;

        showOutput(sum);
    }

    public void reset(View view) {
        sum = 0;

        showOutput(sum);
    }

    public void showOutput(int sum) {
        TextView textView = findViewById(R.id.sum);
        String result = "" + sum;
        textView.setText(result);
    }
}