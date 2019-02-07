package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;

public class CityInfoFragment extends Fragment {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);
        return layout;

        Bundle bundle = getgetIntent().getExtras(); //не смог разобраться (подсвечивает красным)
        boolean wind = false;
        boolean humidity = false;
        boolean pressure = false;
        if (bundle != null) {
            TextView cityName = getActivity().findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));

            wind = bundle.getBoolean(CitiesFragment.CHECK_BOX_WIND);
            humidity = bundle.getBoolean(CitiesFragment.CHECK_BOX_HUMIDITY);
            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
        }

                Button details = getActivity().findViewById(R.id.moreInformation);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                TextView city = getActivity().findViewById(R.id.cityNameInfo);
                String cityName = (String) city.getText();
                Uri uri = Uri.parse("http://yandex.ru/pogoda/" + cityName);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {  //не успел разобраться (подсвечивает красным)
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "\n" +    ////не успел разобраться (подчеркивает красным)
                            "Application does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        Обработка CheckBox Wind
        TextView textWind = (TextView) getActivity().findViewById(R.id.textWind);
        TextView textDirection = (TextView) getActivity().findViewById(R.id.directionWind);

        if (wind == true) {
            textWind.setVisibility(View.VISIBLE);
            textDirection.setVisibility(View.VISIBLE);
        } else {
            textWind.setVisibility(View.GONE);
            textDirection.setVisibility(View.GONE);
        }

        //Обработка CheckBox humidity
        TextView textHumidity = (TextView) getActivity().findViewById(R.id.textHumidity);

        if (humidity == true) {
            textHumidity.setVisibility(View.VISIBLE);
        } else {
            textHumidity.setVisibility(View.GONE);
        }

        //Обработка CheckBox Pressure
        TextView textPressure = (TextView) getActivity().findViewById(R.id.textPressure);

        if (pressure == true) {
            textPressure.setVisibility(View.VISIBLE);
        } else {
            textPressure.setVisibility(View.GONE);
        }
    }

}
