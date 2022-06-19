//Пусковой файл, запускается в момент открытия программы

package com.simon.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_start);
        checkEnteredUser();
    }

//    Проверка, если пользователь уже входил в свой аккаунт. Если не входил, то открывается окно входа и регистрации
//    Если пользователь был авторизован, запускается главное окно.
    private void checkEnteredUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, EnterInActivity.class));
        } else {
            startActivity(new Intent(this,  MainActivity.class));
        }
        this.finish();
    }
}