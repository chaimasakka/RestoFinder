package com.myapp.restofinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.listeners.OnRecipeClickListener;
import com.myapp.restofinder.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesListAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes;
    LayoutInflater inflater;
    OnRecipeClickListener clickListener;

    public RecipesListAdapter(Context context) {
        this.recipes = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.clickListener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        notifyDataSetChanged();
    }

    public void removeRecipe(String key) {
        recipes.removeIf(r -> r.getId().equals(key));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipes.size();
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
        View view = inflater.inflate(R.layout.item_recipe, null);
        TextView name = view.findViewById(R.id.rcipe_name_tv);
        TextView price = view.findViewById(R.id.price_tv);
        ImageView image = view.findViewById(R.id.recipe_iv);
        name.setText(recipes.get(position).getName());
        price.setText(recipes.get(position).getPrice()+"");
        Statics.setImage("recipes/" + recipes.get(position).getId(), image, () -> {
            view.findViewById(R.id.progress_loader).setVisibility(View.GONE);
        });
        if (clickListener != null)
            view.setOnClickListener(v -> clickListener.onClick(recipes.get(position)));
        return view;
    }
}
