package ru.smirnov.test.moretechapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.data.HistoryCars;
import ru.smirnov.test.moretechapp.views.CarInfoActivity;
import ru.smirnov.test.moretechapp.views.adapters.VerticalCarRecyclerAdapter;

import static ru.smirnov.test.moretechapp.views.CarInfoActivity.carImage;

public class HistoryFragment extends Fragment implements VerticalCarRecyclerAdapter.OnClickCallback {
    private final static String TAG = FavoritesFragment.class.getName();

    @BindView(R.id.marketplace_rv)
    private RecyclerView favoritesRv;

    private HistoryCars historyCars;

    private boolean isEmpty = false;

    private View viewContainer;


    public void invalidate() {
        super.onResume();
        if (historyCars.getCars() == null || historyCars.getCars().size() == 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        if (isEmpty) {
            viewContainer.setVisibility(View.VISIBLE);
            favoritesRv.setVisibility(View.GONE);
        } else {
            viewContainer.setVisibility(View.GONE);
            favoritesRv.setVisibility(View.VISIBLE);
            VerticalCarRecyclerAdapter favsAdapter = new VerticalCarRecyclerAdapter(historyCars.getCars(), this);
            LinearLayoutManager layoutManagerRecom = new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);

            favoritesRv.setAdapter(favsAdapter);
            favoritesRv.setLayoutManager(layoutManagerRecom);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyCars = HistoryCars.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, rootView);

        ((TextView)rootView.findViewById(R.id.title_tv)).setText("История поиска");
        ((TextView)rootView.findViewById(R.id.subtitle_tv_fav)).setText("После сканирования авто наиболее вероятное попадет в этот список");

        favoritesRv = rootView.findViewById(R.id.favorite_cars_rv);
        viewContainer = rootView.findViewById(R.id.placeholder_container);
        if (isEmpty) {
            viewContainer.setVisibility(View.VISIBLE);
            favoritesRv.setVisibility(View.GONE);
        } else {
            viewContainer.setVisibility(View.GONE);
            favoritesRv.setVisibility(View.VISIBLE);
            VerticalCarRecyclerAdapter favsAdapter = new VerticalCarRecyclerAdapter(historyCars.getCars(), this);
            LinearLayoutManager layoutManagerRecom = new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);

            favoritesRv.setAdapter(favsAdapter);
            favoritesRv.setLayoutManager(layoutManagerRecom);
        }
        return rootView;
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(getActivity(), CarInfoActivity.class);
        intent.putExtra(carImage, index);
        startActivity(intent);
    }
}
