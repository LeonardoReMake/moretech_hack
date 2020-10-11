package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
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

import static ru.smirnov.test.moretechapp.views.CalculateLoanActivity.cost;
import static ru.smirnov.test.moretechapp.views.RecognitionResultActivity.JSON;

public class CarInfoActivity extends AppCompatActivity {
    private final static String TAG = CarInfoActivity.class.getName();

    public static String carImage = "CAR_IMG";

    private MarketPlaceCars marketPlaceCars;
    private FavoritesCars favoritesCars;

    private Car currentCar;
    private ActivityCarInfoBinding activityCarInfoBinding;
    private TextView paymentTv;

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

    private UserLoan createUserLoan() {
        UserLoan userLoan = new UserLoan();
        userLoan.cost = currentCar.getMinprice();
        userLoan.term = 5;
        userLoan.initialFee = currentCar.getMinprice() * 0.3d;
        userLoan.residualPayment = 0;
        return userLoan;
    }
}
