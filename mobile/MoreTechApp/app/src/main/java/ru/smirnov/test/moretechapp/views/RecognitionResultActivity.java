package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.HistoryCars;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.Car;
import ru.smirnov.test.moretechapp.network.services.CarRecognitionService;
import ru.smirnov.test.moretechapp.network.services.RecognizeRequest;
import ru.smirnov.test.moretechapp.views.adapters.VerticalCarRecyclerAdapter;

import static ru.smirnov.test.moretechapp.views.CarInfoActivity.carImage;

@AndroidEntryPoint
public class RecognitionResultActivity extends AppCompatActivity implements VerticalCarRecyclerAdapter.OnClickCallback {
    private final static String TAG = RecognitionResultActivity.class.getName();

    private CircularProgressIndicator loadingIndicator;
    private RecyclerView carRecognitionResultRv;

    private String url = "http://51.250.24.233:8080/backend/api/v2/recognition";

    private String json = "{\"content\":\"%s\"}";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private MarketPlaceCars marketPlaceCars;
    private HistoryCars historyCars;
    private View placeHolder;

    private VerticalCarRecyclerAdapter marketplaceAdapter;

    @Inject
    CarRecognitionService carRecognitionService;

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

        marketplaceAdapter = new VerticalCarRecyclerAdapter(new ArrayList<>(), this);
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

            Call<ResponseBody> recognizeResponse
                    = carRecognitionService.recognize(new RecognizeRequest(encodedImage));

            recognizeResponse.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        onFailure(call, new InternalError());
                        return;
                    }

                    ResponseBody responseBody = response.body();

                    if (responseBody == null) {
                        onFailure(call, new InternalError());
                        return;
                    }

                    try {
                        JsonObject jsonObject = new Gson().fromJson(responseBody.string(), JsonObject.class);
                        parseResults(jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Не удалось загрузить похожие автомобили", Toast.LENGTH_LONG).show();
                        loadingIndicator.setVisibility(View.GONE);
                        placeHolder.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            "Не удалось загрузить похожие автомобили", Toast.LENGTH_LONG).show();
                    loadingIndicator.setVisibility(View.GONE);
                    placeHolder.setVisibility(View.VISIBLE);
                }
            });

//            OkHttpClient client = new OkHttpClient();
//            String completeJson = "{\"content\":\""+encodedImage+"\"}";
//            Log.d(TAG, String.format("Send json: %s", completeJson));
//            Log.d(TAG, encodedImage);
//            RequestBody requestBody = RequestBody.create(completeJson, JSON);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//            new Thread(() -> {
//                ObjectMapper objectMapper = new ObjectMapper();
//                ResponseBody responseBody = null;
//                try {
//                    responseBody = client.newCall(request).execute().body();
//                    Car[] cars = objectMapper.readValue(responseBody.string(), Car[].class);
//
//                    if (cars.length != 0) {
//                        List<Car> carsList = new ArrayList<>();
//                        Collections.addAll(carsList, cars);
//                        for (Car car : carsList) {
//                            Car carFound = marketPlaceCars.searchForBrandName(car);
//                            if (carFound != null) {
//                                car.setId(carFound.getId());
//                            } else {
//                                Log.d(TAG, car.getBrand() + " " + car.getModel());
//                            }
//                        }
//                        runOnUiThread(() -> {
//                            historyCars.addCar(carsList.get(0));
//                            marketplaceAdapter.addCars(carsList);
//                            loadingIndicator.setVisibility(View.GONE);
//                            carRecognitionResultRv.setVisibility(View.VISIBLE);
//                            marketplaceAdapter.notifyDataSetChanged();
//                        });
//                    } else {
//                        runOnUiThread(() -> {
//                            loadingIndicator.setVisibility(View.GONE);
//                            placeHolder.setVisibility(View.VISIBLE);
//                        });
//                    }
//                } catch (IOException e) {
//                    runOnUiThread(() -> {
//                        Toast.makeText(getApplicationContext(),
//                                "Не удалось загрузить похожие автомобили", Toast.LENGTH_LONG).show();
//                        loadingIndicator.setVisibility(View.GONE);
//                        placeHolder.setVisibility(View.VISIBLE);
//                    });
//                    e.printStackTrace();
//                }
//            }).start();
        }
    }

    @Override
    public void onClick(String id) {
        Intent intent = new Intent(this, CarInfoActivity.class);
        intent.putExtra(carImage, id);
        startActivity(intent);
    }

    private void parseResults(JsonObject jsonObject) {
        boolean confidence = jsonObject.get("confidence").getAsBoolean();

        if (!confidence) {
            loadingIndicator.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            return;
        }

        Map<String, Double> carProbMap = new Gson()
                .fromJson(jsonObject.get("carsWithProbabilities").getAsJsonObject().toString(), HashMap.class);

        List<Map.Entry<String, Double>> list = new ArrayList<>(carProbMap.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        List<Car> cars = new ArrayList<>();
        for (String name : result.keySet()) {
            cars.add(getCarByName(name));
        }

        historyCars.addCar(cars.get(0));
        marketplaceAdapter.addCars(cars);
        loadingIndicator.setVisibility(View.GONE);
        carRecognitionResultRv.setVisibility(View.VISIBLE);
        marketplaceAdapter.notifyDataSetChanged();
    }

    private Car getCarByName(String name) {
        switch (name) {
            case "Hyundai SOLARIS": return marketPlaceCars.searchForBrandName("Hyundai", "SOLARIS");
            case "KIA Rio": return marketPlaceCars.searchForBrandName("KIA", "Rio");
            case "Skoda Octavia": return marketPlaceCars.searchForBrandName("Skoda", "Octavia");
            case "Volkswagen Polo": return marketPlaceCars.searchForBrandName("Volkswagen", "Polo");
            case "Volkswagen Tiguan": return marketPlaceCars.searchForBrandName("Volkswagen", "Tiguan");
        }
        return null;
    }
}
