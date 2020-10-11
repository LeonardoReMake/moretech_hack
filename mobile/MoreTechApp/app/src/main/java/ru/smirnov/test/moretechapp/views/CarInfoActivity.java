package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.FavoritesCars;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.databinding.ActivityCarInfoBinding;
import ru.smirnov.test.moretechapp.models.Car;
import ru.smirnov.test.moretechapp.models.UserLoan;
import ru.smirnov.test.moretechapp.models.UserLoanResult;
import ru.smirnov.test.moretechapp.views.adapters.CarImageSliderAdapter;
import ru.smirnov.test.moretechapp.views.adapters.HorizontalCarRecyclerAdapter;

import static ru.smirnov.test.moretechapp.views.CalculateLoanActivity.cost;
import static ru.smirnov.test.moretechapp.views.RecognitionResultActivity.JSON;

public class CarInfoActivity extends AppCompatActivity implements HorizontalCarRecyclerAdapter.OnClickCallback {
    private final static String TAG = CarInfoActivity.class.getName();

    public static String carImage = "CAR_IMG";

    private MarketPlaceCars marketPlaceCars;
    private FavoritesCars favoritesCars;

    private RecyclerView simCarsRv;

    private Car currentCar;
    private ActivityCarInfoBinding activityCarInfoBinding;
    private TextView paymentTv;

    private HorizontalCarRecyclerAdapter recomendationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCarInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_info);

        favoritesCars = FavoritesCars.getInstance();

        marketPlaceCars = MarketPlaceCars.getInstance();
        Toolbar toolbar = findViewById(R.id.car_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(view -> finish());

        paymentTv = findViewById(R.id.car_loan_payment_tv);
        ImageView favIcon = findViewById(R.id.favorite_flag);
        simCarsRv = findViewById(R.id.recommendation_sim_rv);

        recomendationAdapter = new HorizontalCarRecyclerAdapter(new ArrayList<>(), this);
        LinearLayoutManager layoutManagerRecom = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        simCarsRv.setAdapter(recomendationAdapter);
        simCarsRv.setLayoutManager(layoutManagerRecom);

        Intent intent = getIntent();
        int index = intent.getIntExtra(carImage, 0);
        currentCar = marketPlaceCars.getForId(index);
        boolean isFav = favoritesCars.isFav(index);
        if (isFav) {
            favIcon.setImageResource(R.drawable.fav_full);
        } else {
            favIcon.setImageResource(R.drawable.fav_icon);
        }
        favIcon.setOnClickListener(view -> {
            if (favoritesCars.isFav(index)) {
                favIcon.setImageResource(R.drawable.fav_icon);
                favoritesCars.remove(index);
            } else {
                favIcon.setImageResource(R.drawable.fav_full);
                favoritesCars.addFav(currentCar);
            }
        });

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new CarImageSliderAdapter(this, new ArrayList<>(Arrays.asList(currentCar.getPhotos()))));
        currentCar.setDoors(4);
        currentCar.setBody("Седан");
        currentCar.setColors(5);
        activityCarInfoBinding.setCurrentCar(currentCar);

        findViewById(R.id.car_calc_loan_btn).setOnClickListener(view -> {
            Intent intentCalc = new Intent(this, CalculateLoanActivity.class);
            intentCalc.putExtra(cost, currentCar.getMinprice());
            startActivity(intentCalc);
        });

        calculateLoan();

        calculateSim();
    }

    private void calculateLoan() {
        Log.d(TAG, "Start sending request");
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(createUserLoan());
            String url = "https://gw.hackathon.vtb.ru/vtb/hackathon/calculate";
            Log.d(TAG, String.format("Send json: %s", json));
            RequestBody requestBody = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .addHeader("x-ibm-client-id", "6a1d19ecf31858bebf7f9038a170afb5")
                    .url(url)
                    .post(requestBody)
                    .build();

            new Thread(() -> {
                ResponseBody responseBody = null;
                try {
                    responseBody = client.newCall(request).execute().body();
                    String responseJson = responseBody.string();
                    Log.d(TAG, String.format("Response: %s", responseJson));
                    UserLoanResult userLoanResult = objectMapper.readValue(responseJson, UserLoanResult.class);

                    runOnUiThread(() -> {
                        paymentTv.setText(String.format("от %d ₽/мес ", userLoanResult.payment));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void calculateSim() {
        Log.d(TAG, "Start sending request");
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(createUserLoan());
            BitmapPackage bitmapPackage = new BitmapPackage();
            Picasso.get().load(currentCar.getPhoto()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();

                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    encodedImage = encodedImage.replaceAll("\n", "");
                    OkHttpClient client = new OkHttpClient();
                    String completeJson = "{\"content\":\""+encodedImage+"\"}";
                    Log.d(TAG, String.format("Send json: %s", completeJson));
                    Log.d(TAG, encodedImage);
                    RequestBody requestBody = RequestBody.create(completeJson, JSON);
                    Request request = new Request.Builder()
                            .url("http://172.20.10.3:8080/rest/recognition/suggestion")
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
                                    recomendationAdapter.addCars(carsList);
                                    recomendationAdapter.notifyDataSetChanged();
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private UserLoan createUserLoan() {
        UserLoan userLoan = new UserLoan();
        userLoan.cost = currentCar.getMinprice();
        userLoan.term = 5;
        userLoan.initialFee = currentCar.getMinprice() * 0.3d;
        userLoan.residualPayment = 0;
        return userLoan;
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(this, CarInfoActivity.class);
        intent.putExtra(carImage, index);
        startActivity(intent);
    }

    class BitmapPackage {
        Bitmap bitmap;
    }
}
