package au.edu.jcu.guesstheceleb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ImageView ivImage;
    ImageManager imageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);

//        AssetManager assetManager = getAssets();
//        try {
//            String[] names = assetManager.list("celebs");
//            System.out.println(Arrays.toString(names));
//
//            InputStream stream = assetManager.open("celebs/" + names[0]);
//            Bitmap bitmap = BitmapFactory.decodeStream(stream);
//
//            ivImage.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            System.out.println("failed to get names");
//        }

//        imageManager = new ImageManager(this.getAssets(), "celebs");
//        ivImage.setImageBitmap(imageManager.get(0));

    }
}