package com.myapp.restofinder.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.adapters.RecipesListAdapter;
import com.myapp.restofinder.models.Recipe;
import com.myapp.restofinder.models.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdminRecipesListActivity extends AppCompatActivity {

    private RecipesListAdapter recipeAdapter;
    private ListView recipesLV;
    private FloatingActionButton fab;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recipes_list);
        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        setTitle(restaurant.getName() + " recipes");
        initViews();

        recipeAdapter = new RecipesListAdapter(this);
        recipesLV.setAdapter(recipeAdapter);
        recipeAdapter.setOnRecipeClickListener(r -> {
            Intent intent = new Intent(AdminRecipesListActivity.this, InsertRecipeActivity.class);
            intent.putExtra("restaurant", restaurant);
            intent.putExtra("recipe", r);
            startActivity(intent);
        });

        initDatabse();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRecipesListActivity.this, InsertRecipeActivity.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);
        });

    }

    private void initDatabse() {
        FirebaseDatabase.getInstance().getReference().child("restaurants/" + restaurant.getId() + "/recipes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addFromDb(snapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                recipeAdapter.removeRecipe(snapshot.getKey());
                addFromDb(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                recipeAdapter.removeRecipe(snapshot.getKey());
                Statics.justifyListViewHeightBasedOnChildren(recipesLV);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFromDb(DataSnapshot snapshot) {
        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
        Map<String, String> map = new HashMap<>();
        map.put("id", snapshot.getKey());
        while (iterator.hasNext()) {
            DataSnapshot snap = iterator.next();
            map.put(snap.getKey(), snap.getValue(String.class));
        }
        recipeAdapter.addRecipe(new Recipe(map));
        Statics.justifyListViewHeightBasedOnChildren(recipesLV);
    }

    private void initViews() {
        recipesLV = findViewById(R.id.recipes_lv);
        fab = findViewById(R.id.add_recipe_fab);
    }
}