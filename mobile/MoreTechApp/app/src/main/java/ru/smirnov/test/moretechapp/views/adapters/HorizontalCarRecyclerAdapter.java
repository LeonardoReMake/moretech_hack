package ru.smirnov.test.moretechapp.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.models.Car;

public class HorizontalCarRecyclerAdapter extends RecyclerView.Adapter<HorizontalCarRecyclerAdapter.CarItem> {
    private final static String TAG = HorizontalCarRecyclerAdapter.class.getName();

    private List<Car> cars;

    public interface OnClickCallback {
        void onClick(int index);
    }

    private OnClickCallback callback;

    public HorizontalCarRecyclerAdapter(List<Car> cars, OnClickCallback callback) {
        this.cars = cars;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CarItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.car_item,
                        parent,
                        false);
        return new CarItem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarItem holder, int position) {
        holder.carContainer.setOnClickListener(view -> callback.onClick(cars.get(position).getId()));
        holder.bind(cars.get(position));
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void addCars(List<Car> cars) {
        this.cars.addAll(cars);
    }

    public static class CarItem extends RecyclerView.ViewHolder {
        public ImageView carImg;
        public TextView carTitle;
        public TextView carPrice;
        public View carContainer;

        public CarItem(@NonNull View itemView) {
            super(itemView);

            carContainer = itemView.findViewById(R.id.car_container);
            carImg = itemView.findViewById(R.id.car_img);
            carTitle = itemView.findViewById(R.id.car_title);
            carPrice = itemView.findViewById(R.id.car_price);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Car car) {
//            if (car.getImages() == null || car.getImages().size() == 0) {
//                carImg.setImageResource(R.drawable.car_placeholder);
//            } else {
//                carImg.setImageBitmap(car.getImages().get(0));
//            }
            Picasso.get().load(car.getPhoto()).into(carImg);
            carTitle.setText(String.format("%s %s", car.getCarBrand(), car.getTitle()));
            carPrice.setText(String.format("%d ₽", car.getMinprice()));
        }
    }
}
