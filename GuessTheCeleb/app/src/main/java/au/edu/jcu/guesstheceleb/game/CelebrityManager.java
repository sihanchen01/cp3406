package au.edu.jcu.guesstheceleb.game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.apache.commons.text.WordUtils;

import java.io.IOException;
import java.io.InputStream;

public class CelebrityManager {
    private String assetPath;
    private static String[] imageNames;
    private AssetManager assetManager;

    public CelebrityManager(AssetManager assetManager, String assetPath) {
        this.assetManager = assetManager;
        this.assetPath = assetPath;
        try {
            imageNames = assetManager.list(assetPath);
        } catch (IOException e) {
            imageNames = null;
        }
    }

    public Bitmap get(int i) {
        try {
            InputStream stream = assetManager.open(assetPath+"/" + imageNames[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        } catch (IOException e){
            return null;
        }
    }

    public String getName(int i) {
        // Remove file extension, and replace '-' with ' ' in filename
        String celebNameRaw = imageNames[i].split("\\.")[0].replace("-", " ");
        // Capitalize first letters of first/last name
        return WordUtils.capitalize(celebNameRaw);
    }

    public static int count() {
        return imageNames.length;
    }
}
