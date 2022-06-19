package com.simon.project.utils;

import android.content.Context;
import android.widget.ImageView;

public class Image {

    public static void GetImg(ImageView avatar, String Name) {
        Context context = avatar.getContext();

        int id = context.getResources().getIdentifier(Name, "drawable", context.getPackageName());
        avatar.setImageResource(id);
    }
}
