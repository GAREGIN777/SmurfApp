package com.example.gareginvardanyan;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.onesignal.OneSignal;

public class MyApp extends Application {
    private static final String ONE_SIGNAL_APP_ID = "babc516b-39e5-4c33-9dc3-ceaa67e78956";
    private static final String AF_APP_ID = "YgFzfcdAJcavXYmABVDnDb";

    private static final String AF_TAG = "AF Messages";

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.initWithContext(this,ONE_SIGNAL_APP_ID);

        AppsFlyerLib.getInstance().start(getApplicationContext(), AF_APP_ID, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d(AF_TAG, "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.d(AF_TAG, "Launch failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });


    }

}
