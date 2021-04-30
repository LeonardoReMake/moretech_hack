package ru.smirnov.test.moretechapp.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.smirnov.test.moretechapp.R;
import ru.smirnov.test.moretechapp.models.BankProduct;

public class BankProductRecyclerAdapter extends RecyclerView.Adapter<BankProductRecyclerAdapter.ProductItem> {
    private final static String TAG = HorizontalCarRecyclerAdapter.class.getName();

    private List<BankProduct> products;

    public interface OnClickCallback {
        void onClick(int index);
    }

    private OnClickCallback callback;

    public BankProductRecyclerAdapter(List<BankProduct> products, OnClickCallback callback) {
        this.products = products;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ProductItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bank_product_item,
                        parent,
                        false);
        return new ProductItem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItem holder, int position) {
        holder.cardContainer.setOnClickListener(view -> callback.onClick(position));
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductItem extends RecyclerView.ViewHolder {
        public ImageView cardImg;
        public TextView cardTitle;
        public View cardContainer;

        public ProductItem(@NonNull View itemView) {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.bank_product_container);
            cardImg = itemView.findViewById(R.id.bank_product_icon);
            cardTitle = itemView.findViewById(R.id.bank_product_title);
        }

        @SuppressLint("DefaultLocale")
        public void bind(BankProduct bankProduct) {
            cardTitle.setText(bankProduct.title);
            cardImg.setImageResource(bankProduct.imgRes);
            ((CardView)cardContainer).setBackgroundResource(bankProduct.color);
            ((CardView)cardContainer).setRadius(12f);
        }
    }
}
