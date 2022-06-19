package com.simon.project.map_actions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.type.LatLng;
import com.simon.project.R;

import java.util.Objects;

public class AddMarker {

    public AddMarker(Context context, GoogleMap googleMap, com.google.android.gms.maps.model.LatLng markerLocation, String markerTitle, String category, String markerTag) {
        Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                .position(markerLocation)
                .title(markerTitle)
                .icon(bitmapDescriptorFromVector(context.getApplicationContext(), category))))
                .setTag(markerTag);
    }



    private static BitmapDescriptor bitmapDescriptorFromVector(Context context, String category) {
        Drawable vectorDrawable;
        switch (category) {
            case "Sport":
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dumbbell_training_svgrepo_com);
                break;
            case "Party" :
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_party_blower_birthday_svgrepo_com);
                break;
            case "Pool":
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_swimming_pool_svgrepo_com);
                break;
            case "Walk":
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_running_shoes_8592);
                break;
            case "Art":
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_artist_color_palette_svgrepo_com);
                break;
            default:
                vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_videogame_asset_24);
                break;
        }

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
