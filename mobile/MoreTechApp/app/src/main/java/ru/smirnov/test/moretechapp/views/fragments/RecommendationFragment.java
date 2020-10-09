package ru.smirnov.test.moretechapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.MarketPlaceCars;
import ru.smirnov.test.moretechapp.views.adapters.HorizontalCarRecyclerAdapter;

public class RecommendationFragment extends Fragment {
    private final static String TAG = RecommendationFragment.class.getName();

    @BindView(R.id.bank_products_rv)
    private RecyclerView bankProductsRv;

    @BindView(R.id.recommendation_rv)
    private RecyclerView recommendationRv;

    @BindView(R.id.marketplace_rv)
    private RecyclerView marketplaceRv;

    private MarketPlaceCars marketPlaceCars;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketPlaceCars = MarketPlaceCars.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommendation, container, false);
        ButterKnife.bind(this, rootView);

        marketplaceRv = rootView.findViewById(R.id.marketplace_rv);

        HorizontalCarRecyclerAdapter marketplaceAdapter = new HorizontalCarRecyclerAdapter(marketPlaceCars.getCars(5));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        marketplaceRv.setLayoutManager(layoutManager);
        marketplaceRv.setAdapter(marketplaceAdapter);

        return rootView;
    }
}
