package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.SecondActivity;

public class CitiesFragment extends Fragment {

    public static final String CHECK_BOX_WIND = "checkBoxWind";
    public static final String CHECK_BOX_HUMIDITY = "checkBoxHumidity";
    public static final String CHECK_BOX_PRESSURE = "checkBoxPressure";

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(getActivity(), SecondActivity.class);
            Button btn = getActivity().findViewById(v.getId());
            String cityName = (String) btn.getText();

            if (cityName != null) {
                intent.putExtra(CityInfoFragment.CITY_NAME_EXSTRA, cityName);
            }

            intent.putExtra(CHECK_BOX_WIND, getCheckBoxWind());
            intent.putExtra(CHECK_BOX_HUMIDITY, getCheckBoxHumidity());
            intent.putExtra(CHECK_BOX_PRESSURE, getCheckBoxPressure());
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.to_city_1);
        Button button2 = view.findViewById(R.id.to_city_2);
        Button button3 = view.findViewById(R.id.to_city_3);
        Button button4 = view.findViewById(R.id.to_city_4);

        button.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("cityLookingFor", CityInfoFragment.CITY_NAME_EXSTRA);
//        outState.putSerializable("checkBoxWind", CHECK_BOX_WIND);
//        outState.putSerializable("checkBoxHumidity", CHECK_BOX_HUMIDITY);
//        outState.putSerializable("checkBoxPressure", CHECK_BOX_PRESSURE);
//    }


    public Boolean getCheckBoxWind() {
        CheckBox wind = getActivity().findViewById(R.id.checkBoxWind);
        return wind.isChecked();
    }

    public Boolean getCheckBoxHumidity() {
        CheckBox humidity = getActivity().findViewById(R.id.checkBoxHumidity);
        return humidity.isChecked();
    }

    public Boolean getCheckBoxPressure() {
        CheckBox pressure = getActivity().findViewById(R.id.checkBoxPressure);
        return pressure.isChecked();
    }
}
