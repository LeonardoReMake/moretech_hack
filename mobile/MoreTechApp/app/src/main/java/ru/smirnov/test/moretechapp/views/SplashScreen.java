package ru.smirnov.test.moretechapp.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import javax.inject.Inject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.smirnov.test.moretechapp.CustomApplication;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.Car;
import ru.smirnov.test.moretechapp.network.services.AuthorisationService;
import ru.smirnov.test.moretechapp.network.services.CarsService;
import ru.smirnov.test.moretechapp.network.services.LoginRequest;
import ru.smirnov.test.moretechapp.network.services.LoginResponse;

@AndroidEntryPoint
public class SplashScreen extends AppCompatActivity {
    private final static String TAG = SplashScreen.class.getName();

    private MarketPlaceCars marketPlaceCars;

    private CircularProgressIndicator progressIndicator;

    public static String LOGIN_SP_TAG = "login_credentials";
    public static String PASS_SP_TAG = "pass_credentials";

    public final static String SHARED_PREF = "shared_preferencies";

    private TextView statusTV;

    @Inject
    AuthorisationService authorisationService;

    @Inject
    CarsService carsService;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (isGranted.containsValue(Boolean.FALSE)) {
                    Toast.makeText(this, "Для работы приложения нужны запрашиваемые разрешения", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Permissions dined");
                } else {
                    checkAuthorisation();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        statusTV = findViewById(R.id.statusTV);

        marketPlaceCars = MarketPlaceCars.getInstance();

        checkForPermission();
    }

    private void checkAuthorisation() {
        updateStatus("Проверка авторизации");

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        final String login = sharedPreferences.getString(LOGIN_SP_TAG, null);
        final String pass = sharedPreferences.getString(PASS_SP_TAG, null);

        Call<LoginResponse> authRequest = authorisationService.login(new LoginRequest(login, pass));
        authRequest.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new InternalError());
                    return;
                }

                LoginResponse loginResponse = response.body();

                if (loginResponse == null) {
                    onFailure(call, new InternalError());
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MainActivity.loggedIn, true).apply();
                editor.putString(MainActivity.TOKEN_SP_TAG, loginResponse.getToken()).apply();
                CustomApplication.userToken = loginResponse.getToken();

                updateStatus("Успешно авторизированы");

                prepareData();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, String.format("Error while log in with %s %s. Error: ", login, pass) + t.getMessage());
                updateStatus("Не удалось авторизироваться");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MainActivity.loggedIn, false).apply();

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("MainActivity.loggedIn", false);
                startActivity(intent);
            }
        });
    }

    private void checkForPermission() {
        Log.d(TAG, "Checking permissions");
        updateStatus("Проверка доступов");
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "Permissions missing. Ask for permission");
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA});
        } else {
            checkAuthorisation();
        }
    }

    private void prepareData() {
        updateStatus("Загружаем данные");

        Call<List<Car>> allCarRequest = carsService.getAllCars();
        allCarRequest.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new InternalError());
                    return;
                }

                List<Car> allCarResponse = response.body();
                if (allCarResponse == null) {
                    onFailure(call, new InternalError());
                    return;
                }

                updateStatus("Данные успешно загружены");

                marketPlaceCars.setCars(allCarResponse);
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra(MainActivity.loggedIn, true);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Log.e(TAG, "Error while receiving list of cars. Error: " + t.getMessage());
                updateStatus("Ошибка загрузки данных");

                Toast.makeText(SplashScreen.this,
                        "Не удалось загрузить список автомобилей. Попробуйте позже", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStatus(String text) {
        statusTV.setText(text);
    }
}
