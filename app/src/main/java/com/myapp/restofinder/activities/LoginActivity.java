package com.myapp.restofinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.restofinder.R;
import com.myapp.restofinder.Statics;
import com.myapp.restofinder.activities.admin.AdminMainActivity;
import com.myapp.restofinder.activities.client.MainActivity;
import com.myapp.restofinder.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailET, passwdET;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();

        editor = getSharedPreferences("default", Context.MODE_PRIVATE).edit();

    }

    private void initViews() {
        emailET = findViewById(R.id.email_et);
        passwdET = findViewById(R.id.passwd_et);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.signup_lnk).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup_lnk)
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        else if (v.getId() == R.id.login_btn)
            login();
    }

    private void login() {
        auth.signInWithEmailAndPassword(emailET.getText().toString(), passwdET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Gson builder = new GsonBuilder().create();
                    Map<String, String> data = new HashMap<>();
                    data.put("name", Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
                    data.put("email", emailET.getText().toString());
                    data.put("id", auth.getCurrentUser().getUid());
                    String json = builder.toJson(data);
                    editor.putString("user", json);
                    editor.commit();
                    Statics.USER = new User(data);
                    startActivity(new Intent(LoginActivity.this, emailET.getText().toString().equals("admin@admin.com") ? AdminMainActivity.class : MainActivity.class));
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}