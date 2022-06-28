package com.myapp.restofinder.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.models.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class InsertRestaurantActivity extends AppCompatActivity {

    private ImageView image;
    private EditText nameET;
    private EditText descriptionET;
    private EditText phoneET;
    private Button saveBtn;
    private Bitmap bmp;
    private Restaurant restaurant;
    private Button recipesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_restaurant);

        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");

        requestPermissions(new String[] {Manifest.permission.CAMERA}, 300);

        setTitle(restaurant != null ? "Update " + restaurant.getName() : "Add new Restaurant");

        initViews();

        image.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .start(uri -> {
                        Log.e("uri iss", uri.toString());
                        if (uri != null) {
                        Picasso.get().load(uri).into(image);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    });
        });

        saveBtn.setOnClickListener(v -> {
            save();
        });

        recipesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(InsertRestaurantActivity.this, AdminRecipesListActivity.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);
        });
    }

    private void initViews() {
        image = findViewById(R.id.image);
        nameET = findViewById(R.id.resto_name_et);
        descriptionET = findViewById(R.id.resto_desc_et);
        phoneET = findViewById(R.id.resto_phone_et);
        saveBtn = findViewById(R.id.save_btn);
        recipesBtn = findViewById(R.id.recipe_list_btn);

        if (restaurant != null) {
            findViewById(R.id.progress_loader).setVisibility(View.VISIBLE);
            recipesBtn.setVisibility(View.VISIBLE);
            image.setImageDrawable(null);
            nameET.setText(restaurant.getName());
            descriptionET.setText(restaurant.getDescription());
            phoneET.setText(restaurant.getPhone());
            Statics.setImage("restaurants/" + restaurant.getId(), image, () -> {
                findViewById(R.id.progress_loader).setVisibility(View.GONE);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void save() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("restaurants");
        String key = restaurant != null ? restaurant.getId() : db.push().getKey();
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            FirebaseStorage.getInstance().getReference().child("restaurants").child(key).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saveMap(db, key);
                }
            });
        } else
            saveMap(db, key);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private void saveMap(DatabaseReference db, String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", descriptionET.getText().toString());
        map.put("name", nameET.getText().toString());
        map.put("phone", phoneET.getText().toString());
        Log.e("data iss", new Gson().toJson(map));
        db.child(key).updateChildren(map);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (restaurant != null)
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete restaurant")
                    .setMessage("Are you sure you want to delete " + restaurant.getName() + "?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("restaurants/" + restaurant.getId()).removeValue();
                            finish();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return true;
    }

}