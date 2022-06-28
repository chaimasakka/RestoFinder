package com.myapp.restofinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.models.Review;
import com.willy.ratingbar.ScaleRatingBar;


import java.util.ArrayList;
import java.util.List;


public class CommentsListAdapter extends BaseAdapter {

    ArrayList<Review> reviews;
    LayoutInflater inflater;

    public CommentsListAdapter(Context context) {
        this.reviews = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.clear();
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review reviews) {
        this.reviews.add(reviews);
        notifyDataSetChanged();
    }

    public void removeReview(String key) {
        reviews.removeIf(r -> r.getId().equals(key));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
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
        View view = inflater.inflate(R.layout.item_comment, null);
        Review review = reviews.get(position);
        TextView name = view.findViewById(R.id.name_tv);
        TextView avatar = view.findViewById(R.id.avatar_tv);
        TextView comment = view.findViewById(R.id.comment_tv);
        ScaleRatingBar rating = view.findViewById(R.id.rate_indicator);
        name.setText(review.getName());
        avatar.setText(review.getName().toUpperCase().split(" ")[0].charAt(0) + "" + review.getName().toUpperCase().split(" ")[1].charAt(0));
        comment.setText(review.getComment());
        rating.setRating((float)review.getRating());
        return view;
    }
}
