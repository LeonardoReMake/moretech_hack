package ru.smirnov.test.moretechapp.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.progressindicator.ProgressIndicator;

import java.io.IOException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.Car;

public class SplashScreen extends AppCompatActivity {
    private final static String TAG = SplashScreen.class.getName();

    private MarketPlaceCars marketPlaceCars;

    private ProgressIndicator progressIndicator;

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (isGranted.containsValue(Boolean.FALSE)) {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Permissions dined");
                } else {
                    prepareData();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        marketPlaceCars = MarketPlaceCars.getInstance();

//        progressIndicator = findViewById(R.id.loading_splash_scr);

        checkForPermission();
    }

    private void checkForPermission() {
        Log.d(TAG, "Checking permissions");
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "Permissions missing. Ask for permission");
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA});
        } else {
            prepareData();
        }
    }

    private void prepareData() {
        OkHttpClient client = new OkHttpClient();
        String urlMarketplace = MarketPlaceCars.marketplaceUrl;
        Request request = new Request.Builder()
                .url(urlMarketplace)
                .build();

        Log.d(TAG, String.format("Send request to prepare marketplace cars %s", urlMarketplace));
        new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseBody responseBody = client.newCall(request).execute().body();
                Car[] cars = objectMapper.readValue(responseBody.string(), Car[].class);

                int i = 0;
                for (Car car : cars) {
                    car.setId(i);
                    i++;
                }

                Thread.sleep(3000);

                runOnUiThread(() -> {
                    marketPlaceCars.setCars(cars);
                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                });
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Could not send request");
                e.printStackTrace();
            }
        }).start();
    }
}
