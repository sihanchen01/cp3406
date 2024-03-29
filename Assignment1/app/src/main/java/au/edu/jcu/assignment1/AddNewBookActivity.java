package au.edu.jcu.assignment1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class AddNewBookActivity extends AppCompatActivity {
    ImageView ivBookImage;
    EditText etBookName;
    Button bScan;
    Button bAddBook;

    DatabaseReference databaseRefImageUpload;
    StorageReference storageRefImageUpload;

    Uri myImageUri;
    String bookBarcode;
    String bookName;

    /*
    API url and key for 'barcodelookup.com', it takes barcode input and
    returns book info in json format.

    ! IMPORTANT: THE API COULD FAIL AS IT HAS LIMITED NUMBER OF CALLS FOR FREE TIER ACCOUNT,
               REGISTER NEW ACCOUNT AND REPLACE API_KEY IF NECESSARY
     */
    final String BARCODE_URL = "https://api.barcodelookup.com/v3/products?barcode=";
    final String API_KEY = "1yj2dsx0im12xzovuqen2k1vlyjrbh";


    // Activity Launcher for barcode scanner, and make api call to get book info with barcode
    ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(AddNewBookActivity.this,
                            "Scan is cancelled", Toast.LENGTH_LONG).show();
                } else {
                    bookBarcode = result.getContents();
                    String apiUrl = BARCODE_URL + bookBarcode + "&key=" + API_KEY;
                    // make api call to 'barcodelookup.com' to get book info
                    new JsonTask().execute(apiUrl);
                }
            }
    );

    // Activity launcher to open local gallery to pick image
    ActivityResultLauncher<Intent> uploadImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                myImageUri = data.getData();
                ivBookImage.setImageURI(myImageUri);
            }
        }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        ivBookImage = findViewById(R.id.ivBookImage);
        etBookName = findViewById(R.id.etBookName);
        bAddBook = findViewById(R.id.bAddBook);
        bScan = findViewById(R.id.bScan);

        // Read saved instance state
        if (savedInstanceState != null) {
            bookName = savedInstanceState.getString("bookName");
            myImageUri = savedInstanceState.getParcelable("imageUri");
            ivBookImage.setImageURI(myImageUri);
        }

        // Connect Firebase Database and Storage, specify path to "imageUpload" folder
        databaseRefImageUpload = FirebaseDatabase.getInstance().getReference("imageUpload");
        storageRefImageUpload = FirebaseStorage.getInstance().getReference("imageUpload");

        // Allow user to use local images for book image
        ivBookImage.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            uploadImageLauncher.launch(galleryIntent);
        });


        // Add book into reading list
        bAddBook.setOnClickListener(view -> {
            bookName = etBookName.getText().toString();
            if (!bookName.isEmpty()) {
                uploadImage();

                Intent bookInfo = new Intent(this, MainActivity.class);
                bookName = bookName.substring(0, 1).toUpperCase() + bookName.substring(1);
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

            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan book barcode");
            options.setBeepEnabled(true);
            options.setCameraId(0);
            options.setOrientationLocked(false);

            barcodeLauncher.launch(options);

//            // Uncomment if barcode scanner isn't working,
//            // use a static barcode 9781350168435 to test out 'barcodelookup.com' api
//
//            bookBarcode = "9781350168435";
//            String apiUrl = BARCODE_URL + bookBarcode + "&key=" + API_KEY;
//            new JsonTask().execute(apiUrl);
        });
    }

    // Save instance state so app won't crash when rotate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bookName", bookName);
        outState.putParcelable("imageUri", myImageUri);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {
        /*
        Upload book image to firebaseStorage and log URL to DB
        Set myImageUri to default image if user didn't provide one
        */
        // Use default image if user does not provide one
        if (myImageUri == null) {
            myImageUri = Uri.parse("android.resource://" + AddNewBookActivity.this.getPackageName()
                    + "/drawable/image");
        }

        // Storing image to Firebase Storage, with timestamp as name
        StorageReference fileReference = storageRefImageUpload.child(System.currentTimeMillis()
                + "." + getFileExtension(myImageUri));

        UploadTask uploadTask = fileReference.putFile(myImageUri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            // Get storage object download url to link to database
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddNewBookActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                Uri downloadUri = task.getResult();
                if (downloadUri != null) {
                    bookName = etBookName.getText().toString().trim();
                    bookName = bookName.substring(0, 1).toUpperCase() + bookName.substring(1);
                    Upload upload = new Upload(bookName, downloadUri.toString());
                    String uploadId = databaseRefImageUpload.push().getKey();
                    // Storing book name and book image download url as key value pair in database
                    assert uploadId != null;
                    databaseRefImageUpload.child(uploadId).setValue(upload);
                }
            } else {
                Toast.makeText(AddNewBookActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Implementing JsonTask for making api call to 'barcodelookup.com'
    @SuppressLint("StaticFieldLeak")
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

                // Create a buffer to log result as String, then turn it into Json
                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

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
                // Convert String result into Json object
                JSONObject jsonObject = new JSONObject(result);
                // Traverse json to get book name and book image
                bookName = jsonObject.getJSONArray("products").getJSONObject(0)
                        .getString("title");
                String imageUrl = jsonObject.getJSONArray("products").getJSONObject(0)
                        .getJSONArray("images").getString(0);
                // Set image and title in Views
                new ImageLoadTask(imageUrl, ivBookImage).execute();
                etBookName.setText(bookName);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Toast.makeText(AddNewBookActivity.this,
                        "Invalid barcode, try another one.", Toast.LENGTH_SHORT).show();
            }
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    // Implementing ImageLoadTask to allow downloading book image from given url
    @SuppressLint("StaticFieldLeak")
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final String url;
        private final ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                // Download book image from given url
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
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
            // Assign image bitmap to class variable, and show image in the app
            myImageUri = getImageUriFromBitmap(AddNewBookActivity.this, bitmap, bookName);
            imageView.setImageBitmap(bitmap);
        }
    }

    // Process bitmap image, store it locally on the phone, and return local image uri
    public Uri getImageUriFromBitmap(Context inContext, Bitmap inImage, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                title, null);
        return Uri.parse(path);
    }
}
