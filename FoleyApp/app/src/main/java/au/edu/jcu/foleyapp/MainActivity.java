package au.edu.jcu.foleyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button bNature;
    Button bAnimal;
    Button bHuman;
    Button bTechnology;
    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bNature = findViewById(R.id.bNature);
        bAnimal = findViewById(R.id.bAnimal);
        bHuman = findViewById(R.id.bHuman);
        bTechnology = findViewById(R.id.bTechnology);
        buttons.add(bNature);
        buttons.add(bAnimal);
        buttons.add(bHuman);
        buttons.add(bTechnology);


        for (Button button : buttons){
            button.setOnClickListener(view -> {
                Button b = (Button) view;
                Intent foleyIntent = new Intent(MainActivity.this, FoleyActivity.class);
                String buttonText = b.getText().toString();
                foleyIntent.putExtra("buttonText", buttonText);
                startActivity(foleyIntent);
            });
        }
    }
}