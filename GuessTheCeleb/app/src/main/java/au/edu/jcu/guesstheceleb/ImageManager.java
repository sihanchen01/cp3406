package au.edu.jcu.guesstheceleb;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageManager {
    private String assetPath;
    private String[] imageNames;
    private AssetManager assetManager;

    ImageManager(AssetManager assetManager, String assetPath) {
        this.assetManager = assetManager;
        this.assetPath = assetPath;
        try {
            imageNames = assetManager.list(assetPath);
        } catch (IOException e) {
            imageNames = null;
        }
    }

    Bitmap get(int i) {
        try {
            InputStream stream = assetManager.open(assetPath+"/" + imageNames[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        } catch (IOException e){
            return null;
        }
    }
}
