package com.simon.project.main_fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.simon.project.R;
import com.simon.project.bottom_model_fragments.CreateEventBottomSheet;
import com.simon.project.bottom_model_fragments.JoinEventBottomSheet;
import com.simon.project.controllers.FirebaseEventController;

import java.util.ArrayList;
import java.util.List;

public class MainMapFragment extends Fragment implements OnMapReadyCallback {

    View view;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_map, container, false);
        checkAndRequestPermissions();

//        checkPermission();
        init(view);
        mapListeners();
        return view;
    }
    public boolean checkAndRequestPermissions() {
        int internet = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.INTERNET);
        int loc = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) requireContext(), listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
            return false;
        }

        final LocationManager manager = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
//            Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
        return true;
    }



    private void buildAlertMessageNoGps() {
        /*final AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext())
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null);

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        dialog.dismiss();
                        dialog.cancel();

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.fragment_main_map);
                    }
//                }
dialog
        final AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveBtn = dialog.get ;
            }
        });
        alert.show();*/
        final AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
//                .setView()
//                .setMessage("sd")
                .setTitle("Your GPS seems to be disabled, do you want to enable it?")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {

            Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(view -> {
                // TODO Do something
                dialog.dismiss();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.fragment_main_map);
            });

            Button negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeBtn.setOnClickListener(view -> {
                // TODO Do something
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private SupportMapFragment supportMapFragment;
    private void init(View view) {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private void mapListeners() {
        supportMapFragment.getMapAsync(this);
    }

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng myLocation;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (checkAndRequestPermissions()) {
            googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            moveCameraToUser(googleMap);
            mapClickListener(googleMap, view);
            FirebaseEventController.showAllMarkers(requireActivity(), requireContext(), googleMap);
        } else {
            Toast.makeText(getActivity(), "Map is needed GPS turned on", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.fragment_main_map);
        }
    }


    private void moveCameraToUser(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                checkAndRequestPermissions();
                                myLocation = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(myLocation)      // Sets the center of the map to Mountain View
                                        .zoom(17)                   // Sets the zoom
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
                            }
                        }
                    });
        }
    }

    private  void mapClickListener(GoogleMap googleMap, View view) {
        googleMap.setOnMapClickListener(latLng -> {

        });

        googleMap.setOnMapLongClickListener(latLng -> {
            new CreateEventBottomSheet(view, googleMap, latLng).show(getChildFragmentManager(), "CREATE_EVENT_BOTTOM_SHEET");
        });

        googleMap.setOnMarkerClickListener(marker -> {
            new JoinEventBottomSheet((String)  marker.getTag(), view, getActivity()).show(getChildFragmentManager(), "JOIN_EVENT_BOTTOM_SHEET");
            return false;
        });
    }
}