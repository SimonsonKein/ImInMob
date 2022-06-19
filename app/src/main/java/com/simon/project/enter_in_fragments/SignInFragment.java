package com.simon.project.enter_in_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.simon.project.controllers.FirebaseUserController;
import com.simon.project.R;
import com.simon.project.validators.ValidateResult;

import java.util.HashMap;

public class SignInFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        init(view);
        clickListener(view);
        return view;
    }


    Button signInBtn;
    Button transitionToRegisterBtn;
    private void init(View view) {
        signInBtn = view.findViewById(R.id.sign_in_btn);
        transitionToRegisterBtn = view.findViewById(R.id.transition_to_register_btn);
    }


    private void clickListener(View view) {
        signInBtn.setOnClickListener(v -> {
            HashMap<Integer, EditText> dataMap = getAllData(view);
            if (ValidateResult.validateUserAll(dataMap)) {
                FirebaseUserController.signIn(getActivity(),
                        dataMap.get(R.string.validate_email).getText().toString(),
                        dataMap.get(R.string.validate_password).getText().toString());
            }
        });
        transitionToRegisterBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.fragment_sign_up);
        });
    }

    private HashMap<Integer, EditText> getAllData(View view) {
        EditText emailET = view.findViewById(R.id.sign_in_email_et);
        emailET.setText(emailET.getText().toString().trim());
        EditText passwordET = view.findViewById(R.id.sign_in_password_et);
        passwordET.setText(passwordET.getText().toString().trim());

        HashMap<Integer, EditText> editTextMap = new HashMap<>();
        editTextMap.put(R.string.validate_email, emailET);
        editTextMap.put(R.string.validate_password, passwordET);

        return editTextMap;
    }
}