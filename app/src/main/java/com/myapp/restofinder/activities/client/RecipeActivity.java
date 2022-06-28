package com.myapp.restofinder.activities.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.adapters.RecipesListAdapter;
import com.myapp.restofinder.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private ImageView image;
    private TextView titleTV, descTV, phoneTV;
    private ListView recipesLV;
    private Recipe recipe;
    private ArrayList<Recipe> recipes;
    private RecipesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initViews();

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        recipes = (ArrayList<Recipe>) getIntent().getSerializableExtra("recipes");


        Statics.setImage("recipes/" + recipe.getId(), image, null);
        titleTV.setText(recipe.getName());
        descTV.setText(recipe.getDescription());
        phoneTV.setText(recipe.getPrice()+"");

        adapter = new RecipesListAdapter(this);
        List<Recipe> visibleRecipes = (List<Recipe>) recipes.clone();
        visibleRecipes.removeIf(r -> r.getId().equals(recipe.getId()));
        adapter.setRecipes(visibleRecipes);
        recipesLV.setAdapter(adapter);
        Statics.justifyListViewHeightBasedOnChildren(recipesLV);

        adapter.setOnRecipeClickListener((recipe) -> {
            Intent intent = new Intent(RecipeActivity.this, RecipeActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("recipes", recipes);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        image = findViewById(R.id.recipe_iv);
        titleTV = findViewById(R.id.recipe_name_tv);
        descTV = findViewById(R.id.recipe_desc_tv);
        phoneTV = findViewById(R.id.price_tv);
        recipesLV = findViewById(R.id.recipes_lv);
    }
}