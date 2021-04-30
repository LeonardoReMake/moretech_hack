package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.progressindicator.ProgressIndicator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.HistoryCars;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.Car;
import ru.smirnov.test.moretechapp.views.adapters.VerticalCarRecyclerAdapter;

import static ru.smirnov.test.moretechapp.views.CarInfoActivity.carImage;

public class RecognitionResultActivity extends AppCompatActivity implements VerticalCarRecyclerAdapter.OnClickCallback {
    private final static String TAG = RecognitionResultActivity.class.getName();

    private ProgressIndicator loadingIndicator;
    private RecyclerView carRecognitionResultRv;

    private String url = "http://172.20.10.3:8080/rest/recognition/";

    private String json = "{\"content\":\"%s\"}";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private MarketPlaceCars marketPlaceCars;
    private HistoryCars historyCars;
    private View placeHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_result);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(CameraActivity.BYTE_IMAGE);

        placeHolder = findViewById(R.id.placeholder_container_recres);
//        findViewById(R.id.repeat_camera_btn).setOnClickListener(view -> {
//
//        });
        marketPlaceCars = MarketPlaceCars.getInstance();
        historyCars = HistoryCars.getInstance();
        loadingIndicator = findViewById(R.id.progress_indicator);
        carRecognitionResultRv = findViewById(R.id.recog_result_rv);
        Toolbar toolbar = findViewById(R.id.car_toolbar_recres);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(view -> finish());

        VerticalCarRecyclerAdapter marketplaceAdapter = new VerticalCarRecyclerAdapter(new ArrayList<>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        carRecognitionResultRv.setLayoutManager(layoutManager);
        carRecognitionResultRv.setAdapter(marketplaceAdapter);

        File imgFile = new File(filePath);

        if (imgFile.exists()){
            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); // bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            encodedImage = encodedImage.replaceAll("\n", "");
            OkHttpClient client = new OkHttpClient();
            String completeJson = "{\"content\":\""+encodedImage+"\"}";
            Log.d(TAG, String.format("Send json: %s", completeJson));
            Log.d(TAG, encodedImage);
            RequestBody requestBody = RequestBody.create(completeJson, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            new Thread(() -> {
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseBody responseBody = null;
                try {
                    responseBody = client.newCall(request).execute().body();
                    Car[] cars = objectMapper.readValue(responseBody.string(), Car[].class);

                    if (cars.length != 0) {
                        List<Car> carsList = new ArrayList<>();
                        Collections.addAll(carsList, cars);
                        for (Car car : carsList) {
                            Car carFound = marketPlaceCars.searchForBrandName(car);
                            if (carFound != null) {
                                car.setId(carFound.getId());
                            } else {
                                Log.d(TAG, car.getCarBrand() + " " + car.getTitle());
                            }
                        }
                        runOnUiThread(() -> {
                            historyCars.addCar(carsList.get(0));
                            marketplaceAdapter.addCars(carsList);
                            loadingIndicator.setVisibility(View.GONE);
                            carRecognitionResultRv.setVisibility(View.VISIBLE);
                            marketplaceAdapter.notifyDataSetChanged();
                        });
                    } else {
                        runOnUiThread(() -> {
                            loadingIndicator.setVisibility(View.GONE);
                            placeHolder.setVisibility(View.VISIBLE);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(this, CarInfoActivity.class);
        intent.putExtra(carImage, index);
        startActivity(intent);
    }
}
