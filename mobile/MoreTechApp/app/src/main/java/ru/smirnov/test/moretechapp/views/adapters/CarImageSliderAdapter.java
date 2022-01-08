package ru.smirnov.test.moretechapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.models.CarPhoto;

public class CarImageSliderAdapter extends SliderViewAdapter<CarImageSliderAdapter.SliderAdapterVH> {
    private final static String TAG = CarImageSliderAdapter.class.getName();

    private Context context;
    private List<CarPhoto> mSliderItems = new ArrayList<>();

    public CarImageSliderAdapter(Context context, List<CarPhoto> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public void renewItems(List<CarPhoto> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        viewHolder.bind(mSliderItems.get(position).getLink());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView carImage;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.car_img);
        }

        public void bind(String car) {
            Picasso.get().load(car).into(carImage);
        }
    }
}
