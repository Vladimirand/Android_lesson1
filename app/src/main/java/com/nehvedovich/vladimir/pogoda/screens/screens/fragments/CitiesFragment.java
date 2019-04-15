package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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

import java.util.Objects;

public class CitiesFragment extends Fragment {

    public static final String CHECK_BOX_PRESSURE = "checkBoxPressure";
    public static final String CHECK_BOX_FEEL_LIKE = "checkBoxFeelLike";
    public static final String CHECK_BOX_SUNRISE_AND_SUNSET = "checkBoxSunriseAndSunset";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button1 = view.findViewById(R.id.to_city_1);
        Button button2 = view.findViewById(R.id.to_city_2);
        Button button3 = view.findViewById(R.id.to_city_3);
        Button button4 = view.findViewById(R.id.to_city_4);

        addClickEffect(button1);
        addClickEffect(button2);
        addClickEffect(button3);
        addClickEffect(button4);
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
    }

    //подсветка при нажатии кнопки
    void addClickEffect(View view) {
        Drawable drawableNormal = view.getBackground();
        Drawable drawablePressed = Objects.requireNonNull(view.getBackground().getConstantState()).newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[]{}, drawableNormal);
        view.setBackground(listDrawable);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), SecondActivity.class);
            Button btn = getActivity().findViewById(v.getId());
            String cityName = (String) btn.getText();
            if (cityName != null) {
                intent.putExtra(CityInfoFragment.CITY_NAME_EXSTRA, cityName);
                btn.setPressed(true);
            }
            intent.putExtra(CHECK_BOX_PRESSURE, getCheckBoxPressure());
            intent.putExtra(CHECK_BOX_FEEL_LIKE, getCheckBoxFeelLike());
            intent.putExtra(CHECK_BOX_SUNRISE_AND_SUNSET, getCheckBoxSunriseSunset());
            startActivity(intent);
        }
    };

    public Boolean getCheckBoxPressure() {
        CheckBox pressure = Objects.requireNonNull(getActivity()).findViewById(R.id.checkBoxPressure);
        return pressure.isChecked();
    }

    public Boolean getCheckBoxFeelLike() {
        CheckBox feelLike = Objects.requireNonNull(getActivity()).findViewById(R.id.checkBoxFeelsLike);
        return feelLike.isChecked();
    }

    public Boolean getCheckBoxSunriseSunset() {
        CheckBox feelLike = Objects.requireNonNull(getActivity()).findViewById(R.id.checkBoxSunriseAndSunset);
        return feelLike.isChecked();
    }
}
