package ru.smirnov.test.moretechapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import ru.smirnov.test.moretechapp.views.fragments.FavoritesFragment;
import ru.smirnov.test.moretechapp.views.fragments.HistoryFragment;
import ru.smirnov.test.moretechapp.views.fragments.RecommendationFragment;
import ru.smirnov.test.moretechapp.views.fragments.RegistrationFragment;
import ru.smirnov.test.moretechapp.views.utils.BottomNavigationViewHelper;
import ru.smirnov.test.moretechapp.views.utils.NoSwipePager;

import static ru.smirnov.test.moretechapp.views.SplashScreen.SHARED_PREF;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getName();

    @BindView(R.id.bottom_navigation)
    private BottomNavigationView bottomNavigation;

    private NoSwipePager noSwipePager;

    private final static int MENU = R.menu.bottom_menu;

    private MarketPlaceCars marketPlaceCars;

    public static String loggedIn = "logged";
    public static String TOKEN_SP_TAG = "user_token";

    private RecommendationFragment recommendationFragment;
    private FavoritesFragment favoritesFragment;
    private HistoryFragment historyFragment;

    private AuthorizationFragment authorizationFragment;

    @Inject
    CarsService carsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        marketPlaceCars = MarketPlaceCars.getInstance();

        ((MaterialToolbar)findViewById(R.id.app_toolbar)).setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.exit) {
                clearCreds();
                finishAffinity();
                Intent intent = new Intent(this, SplashScreen.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        createMenu();

        boolean isLoggedIn = getIntent().getExtras().getBoolean(loggedIn, false);

        if (!isLoggedIn) {
            authorizationFragment = new AuthorizationFragment(this::updateUI);
            authorizationFragment.show(getSupportFragmentManager(), "auth");
        }
    }

    private void clearCreds() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        CustomApplication.userToken = null;
    }

    private void createMenu() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        noSwipePager = findViewById(R.id.main_menu_view_pager);

        recommendationFragment = new RecommendationFragment();
        favoritesFragment = new FavoritesFragment();
        historyFragment = new HistoryFragment();

        NoSwipePagerAdapter pagerAdapter = new NoSwipePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(recommendationFragment);
        pagerAdapter.addFragment(historyFragment);
        pagerAdapter.addFragment(favoritesFragment);

        noSwipePager.setPagingEnabled(false);
        noSwipePager.setOffscreenPageLimit(3);
        noSwipePager.setAdapter(pagerAdapter);

        bottomNavigation.inflateMenu(MENU);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed_item:
                noSwipePager.setCurrentItem(0, false);
                break;
            case R.id.coworkers_item:
                noSwipePager.setCurrentItem(1, false);
                historyFragment.invalidate();
                break;
            case R.id.chat_item:
                noSwipePager.setCurrentItem(2, false);
                favoritesFragment.invalidate();
                break;
        }
        return true;
    }

    public void updateUI() {
        Call<List<Car>> allCarResponseCall = carsService.getAllCars();
        allCarResponseCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new InternalError());
                    return;
                }

                if (response.body() == null) {
                    onFailure(call, new InternalError());
                    return;
                }

                List<Car> cars = response.body();
                marketPlaceCars.setCars(cars);
                recommendationFragment.updateCars();
                authorizationFragment.stopLoadingAndDismiss();
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Ошибка загрузки данных. Попробуйте позже", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error while loading list of cars. Error: " + t.getMessage());
            }
        });
    }

    class NoSwipePagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();

        NoSwipePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, String.format("Current position: %d", position));
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @AndroidEntryPoint
    public static class AuthorizationFragment extends DialogFragment {

        private interface ActivityCallback {
            void updateUI();
        }

        @Inject
        AuthorisationService authorisationService;

        private View progress;

        private ActivityCallback callback;

        public AuthorizationFragment(ActivityCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public int getTheme() {
            return R.style.DialogTheme;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_authorization, container, false);
            progress = rootView.findViewById(R.id.progress_indicator_auth);
            rootView.findViewById(R.id.close_dialog_auth).setOnClickListener(view -> dismiss());
            rootView.findViewById(R.id.btn_register).setOnClickListener(view -> {
                RegistrationFragment registrationFragment = new RegistrationFragment();
                registrationFragment.show(getParentFragmentManager(), "register");
            });

            TextInputEditText loginEditText = rootView.findViewById(R.id.user_login);
            TextInputEditText passwordEditText = rootView.findViewById(R.id.user_password);

            final TextInputLayout loginIL = rootView.findViewById(R.id.user_login_input_layout);
            final TextInputLayout passwordIL = rootView.findViewById(R.id.user_password_input_layout);

            rootView.findViewById(R.id.continue_auth).setOnClickListener(view -> {
                if (loginEditText.getText() == null || loginEditText.getText().toString().trim().length() == 0) {
                    loginIL.setError("Неправильный логин");
                    return;
                } else {
                    loginIL.setError(null);
                }

                if (passwordEditText.getText() == null || passwordEditText.getText().toString().trim().length() == 0) {
                    passwordIL.setError("Неверный пароль");
                    return;
                } else {
                    passwordIL.setError(null);
                }

                Call<LoginResponse> loginResponseCall = authorisationService
                        .login(new LoginRequest(
                                loginEditText.getText().toString(),
                                passwordEditText.getText().toString()));

                loginResponseCall.enqueue(new Callback<>() {
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

                        String login = loginEditText.getText().toString();
                        String pass = passwordEditText.getText().toString();
                        String token = loginResponse.getToken();

                        saveCreds(login, pass, token);

                        callback.updateUI();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        passwordIL.setError("Ошибка сервера. Попробуйте позже");
                    }
                });

                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(passwordEditText.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Could not close keyboard");
                }

                progress.setVisibility(View.VISIBLE);
            });

            return rootView;
        }

        private void saveCreds(String login, String pass, String token) {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(MainActivity.loggedIn, true);
            editor.putString(SplashScreen.LOGIN_SP_TAG, login);
            editor.putString(SplashScreen.PASS_SP_TAG, pass);
            editor.putString(MainActivity.TOKEN_SP_TAG, token).apply();
            CustomApplication.userToken = token;
        }

        public void stopLoadingAndDismiss() {
            progress.setVisibility(View.GONE);
            dismiss();
        }

    }
}