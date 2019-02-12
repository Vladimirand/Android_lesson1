package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;

class TemperaturesAdapter extends RecyclerView.Adapter<TemperaturesAdapter.TemperatureViewHolder> {

    private final String[] cities;

    TemperaturesAdapter(String[] cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public TemperatureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_view_item, viewGroup, false);
        TemperatureViewHolder temperatureViewHolder = new TemperatureViewHolder(textView);
        return temperatureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TemperatureViewHolder temperatureViewHolder, int i) {
        temperatureViewHolder.textView.setText(cities[i]);
    }

    @Override
    public int getItemCount() {
        return cities.length;
    }

    class TemperatureViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        TemperatureViewHolder(@NonNull final TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }
}