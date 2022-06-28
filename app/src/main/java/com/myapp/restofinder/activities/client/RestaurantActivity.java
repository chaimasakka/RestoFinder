package com.myapp.restofinder.activities.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.adapters.CommentsListAdapter;
import com.myapp.restofinder.adapters.RecipesListAdapter;
import com.myapp.restofinder.models.Restaurant;
import com.myapp.restofinder.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RestaurantActivity extends AppCompatActivity {

    private ImageView image;
    private TextView titleTV, descTV, phoneTV;
    private ListView recipesLV;
    private ListView commentsLV;
    private Restaurant restaurant;
    private RecipesListAdapter recipeAdapter;
    private CommentsListAdapter commentsAdapter;
    private ScaleRatingBar ratingBar;
    private EditText commentET;
    private ScaleRatingBar commentRatingBar;
    private Button commentBTN;
    private double rating = 0.0;
    private DatabaseReference ref;
    private double reviewRating = 1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        

        initViews();

        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");

        Statics.setImage("restaurants/" + restaurant.getId(), image, null);
        titleTV.setText(restaurant.getName());
        descTV.setText(restaurant.getDescription());
        phoneTV.setText(restaurant.getPhone());

        recipeAdapter = new RecipesListAdapter(this);
        recipeAdapter.setRecipes(restaurant.getRecipes());
        recipesLV.setAdapter(recipeAdapter);

        commentsAdapter = new CommentsListAdapter(this);
        commentsLV.setAdapter(commentsAdapter);
        Statics.justifyListViewHeightBasedOnChildren(recipesLV);
        Statics.justifyListViewHeightBasedOnChildren(commentsLV);

        initDatabase();

        recipeAdapter.setOnRecipeClickListener((recipe) -> {
            Intent intent = new Intent(RestaurantActivity.this, RecipeActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("recipes", (ArrayList) restaurant.getRecipes());
            startActivity(intent);
        });

        commentRatingBar.setOnRatingChangeListener((bar, rating, fromUser) -> {
            reviewRating = rating + .0;
        });

        commentBTN.setOnClickListener(v -> {
            String key = ref.push().getKey();
            Map<String, Object> map = new HashMap<>();
            map.put("name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
            map.put("comment", commentET.getText().toString());
            map.put("rating", String.valueOf(reviewRating));
            ref.child(key).updateChildren(map);
            commentRatingBar.setRating((float) 1.0);
            commentET.setText("");
            reviewRating = 1.0;
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });

        findViewById(R.id.phone_layout).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurant.getPhone()));
            startActivity(intent);
        });
    }

    private void initDatabase() {
        ref = FirebaseDatabase.getInstance().getReference().child("reviews").child("restaurants").child(restaurant.getId());
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("comment added", "true");
                Iterator<DataSnapshot> data = snapshot.getChildren().iterator();
                Map<String, String> map = new HashMap<>();
                map.put("id", snapshot.getKey());
                while (data.hasNext()) {
                    DataSnapshot snap = data.next();
                    map.put(snap.getKey(), snap.getValue(String.class).toString());
                }
                rating += Double.parseDouble(map.get("rating"));
                commentsAdapter.addReview(new Review(map));
                ratingBar.setRating((float) rating / commentsAdapter.getReviews().size());
                Statics.justifyListViewHeightBasedOnChildren(commentsLV);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("comment added", "true");
                Iterator<DataSnapshot> data = snapshot.getChildren().iterator();
                commentsAdapter.removeReview(snapshot.getKey());
                Map<String, String> map = new HashMap<>();
                map.put("id", snapshot.getKey());
                while (data.hasNext()) {
                    DataSnapshot snap = data.next();
                    map.put(snap.getKey(), snap.getValue(String.class).toString());
                }
                rating += Double.parseDouble(map.get("rating"));
                commentsAdapter.addReview(new Review(map));
                ratingBar.setRating((float) rating / commentsAdapter.getReviews().size());
                Statics.justifyListViewHeightBasedOnChildren(commentsLV);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                rating -= Double.valueOf(snapshot.child("rating").getValue(String.class));
                ratingBar.setRating((float) rating / commentsAdapter.getReviews().size());
                commentsAdapter.removeReview(snapshot.getKey());
                Statics.justifyListViewHeightBasedOnChildren(commentsLV);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        image = findViewById(R.id.restaurant_iv);
        titleTV = findViewById(R.id.resto_name_tv);
        descTV = findViewById(R.id.resto_desc_tv);
        phoneTV = findViewById(R.id.phone_tv);
        recipesLV = findViewById(R.id.recipes_lv);
        commentsLV = findViewById(R.id.comments_lv);
        ratingBar = findViewById(R.id.rate_indicator);
        commentBTN = findViewById(R.id.add_review_btn);
        commentET = findViewById(R.id.comment_et);
        commentRatingBar = findViewById(R.id.rate);
    }
}