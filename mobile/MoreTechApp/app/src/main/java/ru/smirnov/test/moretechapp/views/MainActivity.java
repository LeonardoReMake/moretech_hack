package ru.smirnov.test.moretechapp.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.Car;
import ru.smirnov.test.moretechapp.views.fragments.FavoritesFragment;
import ru.smirnov.test.moretechapp.views.fragments.MapFragment;
import ru.smirnov.test.moretechapp.views.fragments.RecommendationFragment;
import ru.smirnov.test.moretechapp.views.utils.BottomNavigationViewHelper;
import ru.smirnov.test.moretechapp.views.utils.NoSwipePager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final static String TAG = MainActivity.class.getName();

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (isGranted.containsValue(Boolean.FALSE)) {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_LONG).show();
                }
            });

    @BindView(R.id.bottom_navigation)
    private BottomNavigationView bottomNavigation;
    private NoSwipePager noSwipePager;

    private final static int MENU = R.menu.bottom_menu;

    private MarketPlaceCars marketPlaceCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkForPermission();

        marketPlaceCars = MarketPlaceCars.getInstance();

        prepareData();



//        findViewById(R.id.openCameraBtn).setOnClickListener(view -> {
//            startActivity(new Intent(this, CameraActivity.class));
//        });
    }

    private void checkForPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA});
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

                runOnUiThread(() -> {
                    marketPlaceCars.setCars(cars);
                    createMenu();
                });
            } catch (IOException e) {
                Log.e(TAG, "Could not send request");
                e.printStackTrace();
            }
        }).start();
    }

    private void createMenu() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        noSwipePager = findViewById(R.id.main_menu_view_pager);

        NoSwipePagerAdapter pagerAdapter = new NoSwipePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new RecommendationFragment());
        pagerAdapter.addFragment(new MapFragment());
        pagerAdapter.addFragment(new FavoritesFragment());

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
                break;
            case R.id.chat_item:
                noSwipePager.setCurrentItem(2, false);
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
}