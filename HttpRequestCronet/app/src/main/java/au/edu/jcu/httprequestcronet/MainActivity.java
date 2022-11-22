package au.edu.jcu.httprequestcronet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

//import org.chromium.net.CronetEngine;
//import org.chromium.net.UrlRequest;

//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    Button bGetJson;
    TextView tvOutput;

    public static final String URL = "https://randomuser.me/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bGetJson = findViewById(R.id.bGetJson);
        tvOutput = findViewById(R.id.tvOutput);

//        CronetEngine.Builder myBuilder = new CronetEngine.Builder(MainActivity.this);
//        CronetEngine cronetEngine = myBuilder.build();
//        Executor executor = Executors.newSingleThreadExecutor();

        RequestQueue queue = Volley.newRequestQueue(this);

        bGetJson.setOnClickListener(view -> {

//            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
//                    URL, new MyUrlRequestCallback(), executor );
//            requestBuilder.setHttpMethod("GET");
//            UrlRequest request = requestBuilder.build();
//            request.start();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    response -> tvOutput.setText(response.substring(0, 100)),
                    error -> tvOutput.setText("Failed."));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,
                    null, response -> {
                try {
                    String title =
                            response.getJSONArray("results").getJSONObject(0).getJSONObject("name"
                            ).getString("title");
                    String firstName =
                            response.getJSONArray("results").getJSONObject(0).getJSONObject("name"
                            ).getString("first");
                    String lastName =
                            response.getJSONArray("results").getJSONObject(0).getJSONObject("name"
                            ).getString("last");
                    String fullName = title + ". " + firstName + " " + lastName;
                    tvOutput.setText("Name: " + fullName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> tvOutput.setText("Failed to retrieve data."));

            queue.add(jsonObjectRequest);
        });
    }
}
