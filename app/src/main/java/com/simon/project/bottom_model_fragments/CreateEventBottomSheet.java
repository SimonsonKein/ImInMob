package com.simon.project.bottom_model_fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.validators.ValidateResult;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreateEventBottomSheet extends BottomSheetDialogFragment {
    private final GoogleMap googleMap;
    private final LatLng markerLocation;
    View rootView;
    public CreateEventBottomSheet(View view, GoogleMap googleMap, LatLng markerLocation) {
        this.rootView = view;
        this.googleMap = googleMap;
        this.markerLocation = markerLocation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event_bottom_sheet, container, false);
        init(view);
        setMaxAmountSpinnerValues();
        clickListener(rootView, view);
        return view;
    }



    EditText eventTitleET, eventDescriptionET;
    TextView eventDateTV, eventTimeTV;
    Button setEventDateBtn, setEventTimeBtn, createEventBtn;
    Spinner eventAmountMembersSpinner, eventCategorySpinner;
    private void init(View view) {
        eventTitleET = view.findViewById(R.id.create_event_title_et);
        eventDescriptionET = view.findViewById(R.id.create_event_description_et);
        eventDateTV = view.findViewById(R.id.create_event_date_tv);
        eventTimeTV = view.findViewById(R.id.create_event_time_tv);
        setEventDateBtn = view.findViewById(R.id.create_event_set_date_btn);
        setEventTimeBtn = view.findViewById(R.id.create_event_set_time_btn);
        createEventBtn = view.findViewById(R.id.create_event_btn);
        eventAmountMembersSpinner = view.findViewById(R.id.create_event_amount_members_spinner);
        eventCategorySpinner = view.findViewById(R.id.create_event_category_spinner);
    }

    private void setMaxAmountSpinnerValues() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 1; i <= getResources().getInteger(R.integer.max_members_default); i++) {
            arrayList.add(i);
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.dropdown_amount_member_spinner_item, arrayList);
        eventAmountMembersSpinner.setAdapter(arrayAdapter);

        String[] arrayList1;
        arrayList1 = getResources().getStringArray(R.array.category_list);
        ArrayAdapter<String> arrayAdapters = new ArrayAdapter<String>(requireContext(),
                R.layout.dropdown_amount_member_spinner_item, arrayList1);
        eventCategorySpinner.setAdapter(arrayAdapters);
    }

    private void setDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    String date = dateFormat.format(calendar.getTime());
                    eventDateTV.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMaxDate(getMaxDate(2));

        datePickerDialog.show();
    }

    private long getMaxDate(int additionalMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, additionalMonth);
        Date maxDate = calendar.getTime();
        return maxDate.getTime();
    }

    private void setTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    String time = new Time(hourOfDay, minute, 0).toString();
                    eventTimeTV.setText(time);
                },
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE,
                true
        );

        timePickerDialog.show();
    }

    private void clickListener(View rootView, View view) {
        createEventBtn.setOnClickListener(v -> {
            HashMap<Integer, Object> createEventDataMap = getAllData();
            if (ValidateResult.validateEventDataAll(getActivity(), createEventDataMap)) {
                FirebaseEventController.createEvent(
                        getActivity(),
                        requireContext(),
                        createEventDataMap,
                        FirebaseAuth.getInstance().getUid(),
                        googleMap,
                        markerLocation,
                        rootView,
                        this);
            }
        });

        setEventDateBtn.setOnClickListener(v -> {
            setDate();
        });

        setEventTimeBtn.setOnClickListener(v -> {
            setTime();
        });
    }

    private HashMap<Integer, Object> getAllData() {
        HashMap<Integer, Object> createEventDataMap = new HashMap<>();
            createEventDataMap.put(R.string.event_title, eventTitleET);
            createEventDataMap.put(R.string.event_date, eventDateTV);
            createEventDataMap.put(R.string.event_time, eventTimeTV);
            createEventDataMap.put(R.string.event_description, eventDescriptionET);
            createEventDataMap.put(R.string.event_amount_members, eventAmountMembersSpinner);
            createEventDataMap.put(R.string.event_category, eventCategorySpinner);
        return createEventDataMap;
    }


}