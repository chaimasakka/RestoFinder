package com.myapp.restofinder.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.listeners.OnRestoClickListener;
import com.myapp.restofinder.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestoListAdapter extends BaseAdapter {

    List<Restaurant> restaurants;
    LayoutInflater inflater;
    OnRestoClickListener clickListener;


    public RestoListAdapter(Context context) {
        this.restaurants = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setOnRestoClickListener(OnRestoClickListener listener) {
        this.clickListener = listener;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants.clear();
        this.restaurants.addAll(restaurants);
        notifyDataSetChanged();
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        notifyDataSetChanged();
    }

    public void removeRestaurant(String key) {
        restaurants.removeIf(r -> r.getId().equals(key));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_restaurant, null);
        TextView name = view.findViewById(R.id.resto_name_tv);
        TextView phone = view.findViewById(R.id.phone_tv);
        ImageView image = view.findViewById(R.id.resto_iv);
        name.setText(restaurants.get(position).getName());
        phone.setText(restaurants.get(position).getPhone());
        view.findViewById(R.id.phone_layout).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurants.get(position).getPhone()));
            view.getContext().startActivity(intent);
        });
        Statics.setImage("restaurants/" + restaurants.get(position).getId(), image, () -> {
            view.findViewById(R.id.progress_loader).setVisibility(View.GONE);
        });
        if (clickListener != null)
            view.setOnClickListener(v -> clickListener.onClick(restaurants.get(position)));
        return view;
    }
}
