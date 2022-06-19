package com.simon.project.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.MainActivity;

import java.util.HashMap;

public class FirebaseUserController {
    public static void signIn(Activity activity, String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SIGN_IN", "Successful enter in");
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    } else {
                        Log.d("SIGN_IN", task.getException().toString());
                    }
                });
    }

    public static void signUp(Activity activity, String email, String nickname, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("email", email);
                        map.put("nickname", nickname);
                        map.put("password", password);
                        FirebaseFirestore.getInstance()
                                .collection("Users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(map)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finish();
                                Log.d("CREATING_FIRESTORE_DATA", "Creating successful");
                            } else {
                                Log.d("CREATING_FIRESTORE_DATA", task1.getException().toString());
                            }
                        });
                        Log.d("SIGN_UP", "Successful sign up");
                    } else {
                        Log.d("SIGN_UP", task.getException().toString());
                    }
                });
    }
}
