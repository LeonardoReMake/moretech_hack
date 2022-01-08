package ru.smirnov.test.moretechapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.models.BankProduct;
import ru.smirnov.test.moretechapp.views.CalculateLoanActivity;
import ru.smirnov.test.moretechapp.views.CameraActivity;
import ru.smirnov.test.moretechapp.views.CarInfoActivity;
import ru.smirnov.test.moretechapp.views.adapters.BankProductRecyclerAdapter;
import ru.smirnov.test.moretechapp.views.adapters.HorizontalCarRecyclerAdapter;

import static ru.smirnov.test.moretechapp.views.CarInfoActivity.carImage;

public class RecommendationFragment extends Fragment implements HorizontalCarRecyclerAdapter.OnClickCallback {
    private final static String TAG = RecommendationFragment.class.getName();

    @BindView(R.id.bank_products_rv)
    private RecyclerView bankProductsRv;

    @BindView(R.id.recommendation_rv)
    private RecyclerView recommendationRv;

    @BindView(R.id.marketplace_rv)
    private RecyclerView marketplaceRv;

    private List<BankProduct> bankProductList;

    private MarketPlaceCars marketPlaceCars;

    private HorizontalCarRecyclerAdapter marketplaceAdapter;
    private HorizontalCarRecyclerAdapter recommendationAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketPlaceCars = MarketPlaceCars.getInstance();

        bankProductList = new ArrayList<>();
        bankProductList.add(new BankProduct("Калькулятор автокредита", R.drawable.calc_icon_prod, "", R.color.cyan));
        bankProductList.add(new BankProduct("Оформление автокредита", R.drawable.auto_loan_prod_icon, "", R.color.cyan_2));
        bankProductList.add(new BankProduct("Оформление КАСКО", R.drawable.kasko_bank_prod_icon, "", R.color.cyan_3));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommendation, container, false);
        ButterKnife.bind(this, rootView);

        rootView.findViewById(R.id.btn_start_camera).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        marketplaceRv = rootView.findViewById(R.id.marketplace_rv);
        bankProductsRv = rootView.findViewById(R.id.bank_products_rv);
        recommendationRv = rootView.findViewById(R.id.recommendation_rv);

        recommendationAdapter = new HorizontalCarRecyclerAdapter(marketPlaceCars.getCars(10), this);
        LinearLayoutManager layoutManagerRecom = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recommendationRv.setAdapter(recommendationAdapter);
        recommendationRv.setLayoutManager(layoutManagerRecom);

        BankProductRecyclerAdapter bankProductRecyclerAdapter = new BankProductRecyclerAdapter(bankProductList, index -> {
            if (index == 0) {
                Intent intent = new Intent(requireContext(), CalculateLoanActivity.class);
                intent.putExtra("isApplicable", false);
                startActivity(intent);
            }
        });
        bankProductsRv.setAdapter(bankProductRecyclerAdapter);
        LinearLayoutManager bankProdsLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        bankProductsRv.setLayoutManager(bankProdsLayoutManager);

        marketplaceAdapter = new HorizontalCarRecyclerAdapter(marketPlaceCars.getCars(5), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        marketplaceRv.setLayoutManager(layoutManager);
        marketplaceRv.setAdapter(marketplaceAdapter);

        return rootView;
    }

    public void updateCars() {
        recommendationAdapter.setCars(marketPlaceCars.getCars(10));
        recommendationAdapter.notifyDataSetChanged();
        marketplaceAdapter.setCars(marketPlaceCars.getCars(5));
        marketplaceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String id) {
        Intent intent = new Intent(getActivity(), CarInfoActivity.class);
        intent.putExtra(carImage, id);
        startActivity(intent);
    }
}
