package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.database.City;
import com.nehvedovich.vladimir.pogoda.screens.database.CityService;
import com.nehvedovich.vladimir.pogoda.screens.database.ParcelableObjectList;
import com.nehvedovich.vladimir.pogoda.screens.screens.MainActivity;
import com.nehvedovich.vladimir.pogoda.screens.screens.SecondActivity;
import com.nehvedovich.vladimir.pogoda.screens.utils.RecyclerListAdapter;
import com.nehvedovich.vladimir.pogoda.screens.utils.WConstants;
import com.nehvedovich.vladimir.pogoda.screens.utils.WData;

import java.util.List;
import java.util.Objects;

public class CitiesFragment extends Fragment {

    private WData simpleView;

    public static final String CHECK_BOX_PRESSURE = "checkBoxPressure";
    public static final String CHECK_BOX_SUNRISE_AND_SUNSET = "checkBoxSunriseAndSunset";

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<City> citiesList = null;
            try {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    ParcelableObjectList<City> objectList = bundle.getParcelable(WConstants.CITIES_LIST);
                    if (objectList != null) {
                        citiesList = objectList.getObjectList();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            configureRecycleView(getView(), citiesList);
        }
    };

    public interface OnItemClickListener {
        void itemClicked(City city);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    private OnItemClickListener onClickListener = new OnItemClickListener() {

        @Override
        public void itemClicked(City city) {
            simpleView = constructSimpleView(city);

            MainActivity.coordPut = false;
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), SecondActivity.class);
            intent.putExtra(CityInfoFragment.CITY_NAME_EXTRA, city.getName());

            intent.putExtra(CHECK_BOX_PRESSURE, getCheckBoxPressure());
            intent.putExtra(CHECK_BOX_SUNRISE_AND_SUNSET, getCheckBoxSunriseSunset());
            startActivity(intent);
        }
    };

    private WData constructSimpleView(City city) {
        return new WData(
                city);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerCitiesReceiver();
        getCitiesList();
    }

    private void getCitiesList() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, CityService.class);
            activity.startService(intent);
        }
    }

    private void configureRecycleView(@Nullable View view, List<City> citiesList) {
        if (view == null || citiesList == null) {
            return;
        }
        RecyclerView recyclerView = view.findViewById(R.id.cities);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerListAdapter adapter = new RecyclerListAdapter(citiesList, onClickListener);
        recyclerView.setAdapter(adapter);
    }


    private void registerCitiesReceiver() {
        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(
                WConstants.SERVICE_CITY_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        Activity activity = getActivity();
        if (activity != null) {
            activity.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerCitiesReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        Activity activity = getActivity();
        if (activity != null) {
            activity.unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(WConstants.WEATHER, simpleView);
    }

    public Boolean getCheckBoxPressure() {
        CheckBox pressure = Objects.requireNonNull(getActivity()).findViewById(R.id.checkBoxPressure);
        return pressure.isChecked();
    }

    public Boolean getCheckBoxSunriseSunset() {
        CheckBox feelLike = Objects.requireNonNull(getActivity()).findViewById(R.id.checkBoxSunriseAndSunset);
        return feelLike.isChecked();
    }


}
