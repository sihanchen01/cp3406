package au.edu.jcu.uploadtofiredb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddNewBookActivity extends AppCompatActivity {
    ImageView ivBookImage;
    EditText etBookName;
    Button bScan;
    Button bAddBook;

    private String bookBarcode;

    private final String BARCODE_URL = "https://api.barcodelookup.com/v3/products?barcode=";
    private final String API_KEY = "ql7sbxolz5lbsihyxuda3lhifvyi9w";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        ivBookImage = findViewById(R.id.ivBookImage);
        etBookName = findViewById(R.id.etBookName);
        bAddBook = findViewById(R.id.bAddBook);
        bScan = findViewById(R.id.bScan);

        bAddBook.setOnClickListener(view -> {
            String bookName = etBookName.getText().toString();
            if (!bookName.isEmpty()) {
                Intent bookInfo = new Intent(this, MainActivity.class);
                bookInfo.putExtra("bookName", bookName);
                setResult(RESULT_OK, bookInfo);
                finish();
            } else {
                Toast.makeText(AddNewBookActivity.this, "Book Name can not be empty!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Use camera to scan barcode to get book info
        bScan.setOnClickListener(view -> {

//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Scan book barcode");
//            options.setBeepEnabled(true);
//            options.setCameraId(0);
//            options.setOrientationLocked(false);
//
//            barcodeLauncher.launch(options);

            // to test api, as no real phone to scan barcode
            // TODO: remove hardcode book barcode
            bookBarcode = "9781788168595";

            String apiUrl = BARCODE_URL + bookBarcode + "&key=" + API_KEY;
            new JsonTask().execute(apiUrl);

        });

        // TODO: allow user to upload book image
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
        registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(AddNewBookActivity.this,
                        "Scan is cancelled", Toast.LENGTH_LONG).show();
            } else {
                bookBarcode = result.getContents();
                Toast.makeText(AddNewBookActivity.this,
                        "Scanned:" + result.getContents(), Toast.LENGTH_LONG).show();
            }
        });


    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd = new ProgressDialog(AddNewBookActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));


                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                // Get book image and title from API
                String bookTitle = jsonObject.getJSONArray("products").getJSONObject(0)
                        .getString("title");
                String imageUrl = jsonObject.getJSONArray("products").getJSONObject(0)
                        .getJSONArray("images").getString(0);
                // Set image and title in Views
                new ImageLoadTask(imageUrl, ivBookImage).execute();
                etBookName.setText(bookTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        public ImageLoadTask (String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
