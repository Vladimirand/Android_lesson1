package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;

public class CityInfoFragment extends Fragment {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);

        Bundle bundle = getArguments();

        boolean wind = false;
        boolean humidity = false;
        boolean pressure = false;

        if (bundle != null) {
            TextView cityName = layout.findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));

            wind = bundle.getBoolean(CitiesFragment.CHECK_BOX_WIND);
            humidity = bundle.getBoolean(CitiesFragment.CHECK_BOX_HUMIDITY);
            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
        }

//        Обработка CheckBox Wind
        TextView textWind = (TextView) layout.findViewById(R.id.textWind);
        TextView textDirection = (TextView) layout.findViewById(R.id.directionWind);

        if (wind == true) {
            textWind.setVisibility(View.VISIBLE);
            textDirection.setVisibility(View.VISIBLE);
        } else {
            textWind.setVisibility(View.GONE);
            textDirection.setVisibility(View.GONE);
        }

        //Обработка CheckBox humidity
        TextView textHumidity = (TextView) layout.findViewById(R.id.textHumidity);

        if (humidity == true) {
            textHumidity.setVisibility(View.VISIBLE);
        } else {
            textHumidity.setVisibility(View.GONE);
        }

        //Обработка CheckBox Pressure
        TextView textPressure = (TextView) layout.findViewById(R.id.textPressure);

        if (pressure == true) {
            textPressure.setVisibility(View.VISIBLE);
        } else {
            textPressure.setVisibility(View.GONE);
        }

        return layout;
    }
}
