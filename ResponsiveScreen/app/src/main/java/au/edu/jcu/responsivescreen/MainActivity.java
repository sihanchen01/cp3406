package au.edu.jcu.responsivescreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    BlankFragment blankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentManager fragmentManager =getSupportFragmentManager();
//        blankFragment = (BlankFragment) fragmentManager.findFragmentById(R.id.fragBlankOG);
    }
}