package com.simon.project.validators;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.project.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValidateResult {
    public static boolean validateUser(int VALIDATE_TYPE, EditText validatingStringET) {
        @NotNull
        String validatingString = validatingStringET.getText().toString();
        ArrayList<String> list = Validate.validateUserData(VALIDATE_TYPE, validatingString);
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                s += list.get(i) + "\n";
            }
        }
        if (!s.equals("")) {
            validatingStringET.setError(s);
        }
        return s.equals("");
    }


    public static boolean validateUserAll(HashMap<Integer, EditText> dataMap) {
        boolean validateAccepted = true;
        for (Map.Entry<Integer, EditText> entry: dataMap.entrySet()){
            if (!validateUser(entry.getKey(), entry.getValue())) {
                validateAccepted = false;
            }
        }
        return validateAccepted;
    }

    public static boolean validateEventDataAll(Activity activity, HashMap<Integer, Object> dataMap) {
        boolean validateAccepted = true;
        for (Map.Entry<Integer, Object> entry: dataMap.entrySet()) {
            switch (entry.getKey()) {
                case R.string.event_title:
                case R.string.event_description:
                    EditText validatingStringET = (EditText) dataMap.get(entry.getKey());
                    if (!validateEventInputData(entry.getKey(), validatingStringET)){
                        validateAccepted = false;
                    }
                    break;
                case R.string.event_date:
                case R.string.event_time:
                    TextView validatingStringTV = (TextView) dataMap.get(entry.getKey());
                    if (!validateEventDateAndTimeData(activity, entry.getKey(), validatingStringTV)) {
                        validateAccepted = false;
                    }
                    break;
            }
        }
        return validateAccepted;
    }

    private static boolean validateEventDateAndTimeData(Activity activity, Integer VALIDATE_TYPE, TextView validatingStringTV) {
        @NotNull
        String validatingString = validatingStringTV.getText().toString();
        if (validatingString.equals("Event Date") || validatingString.equals("Event Time"))
            return false;
        ArrayList<String> list = Validate.validateEventData(VALIDATE_TYPE, validatingString);
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                s += list.get(i) + "\n";
            }
        }
        if (!s.equals("")) {
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        }
        return s.equals("");
    }

    public static boolean validateEventInputData(int VALIDATE_TYPE, EditText validatingStringET) {
        @NotNull
        String validatingString = validatingStringET.getText().toString();
        ArrayList<String> list = Validate.validateEventData(VALIDATE_TYPE, validatingString);
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                s += list.get(i) + "\n";
            }
        }
        if (!s.equals("")) {
            validatingStringET.setError(s);
        }
        return s.equals("");
    }
}
