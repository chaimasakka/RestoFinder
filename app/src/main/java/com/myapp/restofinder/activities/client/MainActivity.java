package com.myapp.restofinder.activities.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.activities.SplashActivity;
import com.myapp.restofinder.adapters.RestoListAdapter;
import com.myapp.restofinder.models.Recipe;
import com.myapp.restofinder.models.Restaurant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ListView restaurantsLV;
    private RestoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();

        adapter = new RestoListAdapter(this);
        restaurantsLV.setAdapter(adapter);
        Statics.justifyListViewHeightBasedOnChildren(restaurantsLV);

        adapter.setOnRestoClickListener(rest -> {
            Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
            intent.putExtra("restaurant", rest);
            startActivity(intent);
        });

        database.child("restaurants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Iterator<DataSnapshot> data = snapshot.getChildren().iterator();
                Map<String, Object> map = new HashMap<>();
                    map.put("id", snapshot.getKey());
                while (data.hasNext()) {
                    DataSnapshot snap = data.next();
                    if (!snap.getKey().equals("recipes"))
                        map.put(snap.getKey(), snap.getValue(String.class));
                    else {
                        List<Recipe> recipes = new ArrayList<>();
                        Iterator<DataSnapshot> recipesSnapshot = snap.getChildren().iterator();
                        while (recipesSnapshot.hasNext()) {
                            DataSnapshot rs = recipesSnapshot.next();
                            Map<String, String> recipesMap = new HashMap<>();
                            recipesMap.put("id", rs.getKey());
                            Log.e("key isss", rs.getKey());
                            Iterator<DataSnapshot> recipeSnap = rs.getChildren().iterator();
                            while (recipeSnap.hasNext()) {
                                DataSnapshot recipesSnap = recipeSnap.next();
                                recipesMap.put(recipesSnap.getKey(), recipesSnap.getValue(String.class));
                            }
                            recipes.add(new Recipe(recipesMap));
                            map.put("recipes", recipes);
                        }
                    }
                }
                adapter.addRestaurant(new Restaurant(map));
                Statics.justifyListViewHeightBasedOnChildren(restaurantsLV);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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
        restaurantsLV = findViewById(R.id.restaurants_lv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences.Editor editor = getSharedPreferences("default", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
        }
        return true;
    }
}