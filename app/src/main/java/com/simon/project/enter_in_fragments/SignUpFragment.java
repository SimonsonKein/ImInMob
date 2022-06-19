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

public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(view);
        clickListener(view);
        return view;
    }

    Button signUpBtn;
    Button backToLoginBtn;
    private void init(View view) {
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        backToLoginBtn = view.findViewById(R.id.back_to_login_btn);
    }

    private void clickListener(View view) {
        signUpBtn.setOnClickListener(v -> {
            HashMap<Integer, EditText> dataMap = getAllData(view);
            if (ValidateResult.validateUserAll(dataMap)) {
                FirebaseUserController.signUp(getActivity(),
                        dataMap.get(R.string.validate_email).getText().toString(),
                        dataMap.get(R.string.validate_nickname).getText().toString(),
                        dataMap.get(R.string.validate_password).getText().toString());
            }
        });

        backToLoginBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.fragment_sign_in);
        });
    }

    private HashMap<Integer, EditText> getAllData(View view) {
        EditText emailET = view.findViewById(R.id.sign_up_email_et);
        emailET.setText(emailET.getText().toString().trim());
        EditText nicknameET = view.findViewById(R.id.sign_up_nickname_et);
        nicknameET.setText(nicknameET.getText().toString().trim());
        EditText passwordET = view.findViewById(R.id.sign_up_password_et);
        passwordET.setText(passwordET.getText().toString().trim());

        HashMap<Integer, EditText> editTextMap = new HashMap<>();
        editTextMap.put(R.string.validate_email, emailET);
        editTextMap.put(R.string.validate_nickname, nicknameET);
        editTextMap.put(R.string.validate_password, passwordET);

        return editTextMap;
    }
}