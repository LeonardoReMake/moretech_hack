package ru.smirnov.test.moretechapp.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.views.fragments.FavoritesFragment;
import ru.smirnov.test.moretechapp.views.fragments.HistoryFragment;
import ru.smirnov.test.moretechapp.views.fragments.RecommendationFragment;
import ru.smirnov.test.moretechapp.views.utils.BottomNavigationViewHelper;
import ru.smirnov.test.moretechapp.views.utils.NoSwipePager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getName();

    @BindView(R.id.bottom_navigation)
    private BottomNavigationView bottomNavigation;

    private NoSwipePager noSwipePager;

    private final static int MENU = R.menu.bottom_menu;

    private MarketPlaceCars marketPlaceCars;

    private String loggedIn = "logged";

    private FavoritesFragment favoritesFragment;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        marketPlaceCars = MarketPlaceCars.getInstance();

        createMenu();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(loggedIn, false);

        if (!isLoggedIn) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(loggedIn, false);
            editor.apply();

            AuthorizationFragment authorizationFragment = new AuthorizationFragment();
            authorizationFragment.show(getSupportFragmentManager(), "auth");
        }
//        findViewById(R.id.openCameraBtn).setOnClickListener(view -> {
//            startActivity(new Intent(this, CameraActivity.class));
//        });
    }

    private void createMenu() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        noSwipePager = findViewById(R.id.main_menu_view_pager);

        favoritesFragment = new FavoritesFragment();
        historyFragment = new HistoryFragment();

        NoSwipePagerAdapter pagerAdapter = new NoSwipePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new RecommendationFragment());
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

    public static class AuthorizationFragment extends DialogFragment {

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

            rootView.findViewById(R.id.close_dialog_auth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            TextInputEditText phoneEditText = rootView.findViewById(R.id.user_phone);
            rootView.findViewById(R.id.continue_auth).setOnClickListener(view -> {
                if (phoneEditText.getText() == null || phoneEditText.getText().toString().length() < 5) {
                    ((TextInputLayout)rootView.findViewById(R.id.user_phone_input_layout)).setError("Неправильный номер телефона");
                } else {
                    try {
                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (inputManager != null) {
                            inputManager.hideSoftInputFromWindow(phoneEditText.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "");
                    }
                    View progress = rootView.findViewById(R.id.progress_indicator_auth);
                    progress.setVisibility(View.VISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(AuthorizationFragment.this::dismiss, 3000);
                }
            });

            return rootView;
        }

    }
}