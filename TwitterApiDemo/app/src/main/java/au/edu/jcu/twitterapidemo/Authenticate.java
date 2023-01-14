package au.edu.jcu.twitterapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Authenticate extends AppCompatActivity {
    WebView wvAuth;
    TextView tvAuthInfo;
    String oauthVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_authenticate);
//
//        wvAuth = findViewById(R.id.wvAuth);
//        tvAuthInfo = findViewById(R.id.tvAuthInfo);
//
//        wvAuth.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//
//                tvAuthInfo.setText("working...");
//                String message;
//                if (url.startsWith("https://www.jcu.edu.au")) {
//                    Uri uri = Uri.parse(url);
//                    oauthVerifier = uri.getQueryParameter("oauth_verifier");
//                    if (oauthVerifier != null) {
//                        Log.i("Authenticate", "authenticated!");
//                        message = "Authenticated!";
//                        updateTwitterConfiguration();
//                        wvAuth.loadData("done", "text/html", null);
//                    } else {
//                        message = "Not authenticated...";
//                    }
//                    tvAuthInfo.setText(message);
//                }
//            }
//        });
//
//        Background.run(() -> {
//           try {
//               RequestToken requestToken = twitter.getOAuthRequestToken();
//               final String requestUrl = requestToken.getAuthenticationURL();
//               runOnUiThread(() -> wvAuth.loadUrl(requestUrl));
//           } catch (TwitterException e) {
//               e.printStackTrace();
//               runOnUiThread(() -> tvAuthInfo.setText(e.toString()));
//           }
//        });
    }

//    private void updateTwitterConfiguration() {
//        Background.run(() -> {
//            try{
//                AccessToken accessToken = twitter.getOAuthAccessToken(oauthVerifier);
//                twitter.setOAuthAccessToken(accessToken);
//            } catch (TwitterException e) {
//                Log.e("Authenticate", e.toString());
//                runOnUiThread(() -> tvAuthInfo.setText(e.toString()));
//            }
//        });
//    }
}