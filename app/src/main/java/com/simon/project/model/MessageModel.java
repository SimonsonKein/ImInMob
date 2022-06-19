package com.simon.project.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageModel{

    private final String message;
    private final String senderNickname;

    public MessageModel(String message, String senderNickname) {
        this.message = message;
        this.senderNickname = senderNickname;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSenderID() {
        return this.senderNickname;
    }
}
