package com.myapp.restofinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.activities.admin.AdminMainActivity;
import com.myapp.restofinder.activities.client.MainActivity;
import com.myapp.restofinder.models.User;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("default", Context.MODE_PRIVATE);
        if (prefs.contains("user")) {
            Statics.USER = new Gson().fromJson(prefs.getString("user", null), User.class);
            startActivity(new Intent(this, Statics.USER.getEmail().equals("admin@admin.com") ? AdminMainActivity.class : MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();

    }
}