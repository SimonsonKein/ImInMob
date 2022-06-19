package com.simon.project.validators;

import com.simon.project.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static ArrayList<String> validateUserData(int VALIDATE_TYPE, String validatingString) {
        ArrayList<String> errors = new ArrayList<>();
        switch (VALIDATE_TYPE) {
            case R.string.validate_username:
                errors.add(checkEmptyString(validatingString));
                errors.add(checkSymbols(validatingString));
                errors.add(checkLength(validatingString));
                break;
            case R.string.validate_nickname:
                errors.add(checkEmptyString(validatingString));
                errors.add(checkSymbols(validatingString));
                errors.add(checkLength(validatingString));
                break;
            case R.string.validate_email:
                errors.add(checkEmptyString(validatingString));
                errors.add(checkEmail(validatingString));
                break;
            case R.string.validate_password:
                errors.add(checkEmptyString(validatingString));
                errors.add(checkLength(validatingString));
        }
        return errors;
    }

    private static String deleteSpace(String validatingString) {
        return validatingString.trim();
    }

    static String checkEmptyString(String validatingString) {
        if (validatingString.isEmpty()) {
            return "is empty";
        }
        return null;
    }

    public static ArrayList<String> validateEventData(int VALIDATE_TYPE, String validatingString) {
        ArrayList<String> errors = new ArrayList<>();
        switch (VALIDATE_TYPE) {
            case R.string.event_title:
            case R.string.event_description:
                errors.add(checkEmptyString(validatingString));
                break;
            case R.string.event_date:
            case R.string.event_time:
                errors.add(checkDateAndTime(validatingString));
                break;

        }
        return errors;
    }


    void checkDecimals(String validatingString) {

    }

    static String checkDateAndTime(String validatingString) {
        switch (validatingString) {
            case "DATE":
                return "choose date";
            case "TIME":
                return "choose time";
            default:
                return null;
        }
    }

    static String checkEmail(String validatingString) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(validatingString);
        if (!matcher.find()) {
            return "email is incorrect";
        }
        return null;
    }

    static String checkLength(String validatingString) {
        if (validatingString.length() < 6) {
            return "must be 5 - 15 symb";
        }
        return null;
    }

    static String checkSymbols(String validatingString) {
        String specialSymbols = "[`!@#$%^&*()+\\-=\\[\\]\\{};':\\\"\\\\|,.<>\\/?~ ]+";
        Pattern pattern = Pattern.compile(specialSymbols);
        Matcher matcher = pattern.matcher(validatingString);
        if (matcher.find()) {
            return "remove symbols";
        }
        return null;
    }

}
