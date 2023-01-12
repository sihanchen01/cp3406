package com.example.cpjjh.twitterdemo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Authenticate extends AppCompatActivity {
    private WebView webView;
    private TextView info;
    private final Twitter twitter = TwitterFactory.getSingleton();
    private String oauthVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        info = findViewById(R.id.info);
        webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                info.setText(getString(R.string.working));
                String message;
                if (url.startsWith("https://www.jcu.edu.au")) {
                    Uri uri = Uri.parse(url);
                    oauthVerifier = uri.getQueryParameter("oauth_verifier");
                    if (oauthVerifier != null) {
                        Log.i("Authenticate", "authenticated!");
                        message = "Authenticated!";
                        updateTwitterConfiguration();
                        webView.loadData("done", "text/html", null);
                    } else {
                        message = "Not authenticated...";
                    }
                    info.setText(message);
                }
            }
        });

        // present twitter login for authentication
        Background.run(() -> {
            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                final String requestUrl = requestToken.getAuthenticationURL();
                runOnUiThread(() -> webView.loadUrl(requestUrl));

            } catch (final Exception e) {
                Log.i("Authenticate", e.toString());
                runOnUiThread(() -> info.setText(e.toString()));
            }
        });
    }

    private void updateTwitterConfiguration() {
        Background.run(() -> {
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(oauthVerifier);
                twitter.setOAuthAccessToken(accessToken);
            } catch (final Exception e) {
                Log.e("Authenticate", e.toString());
                runOnUiThread(() -> info.setText(e.toString()));
            }
        });
    }
}
