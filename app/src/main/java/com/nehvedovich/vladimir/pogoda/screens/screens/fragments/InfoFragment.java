package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nehvedovich.vladimir.pogoda.R;

public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] history = getActivity().getResources().getStringArray(R.array.Temperatures);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        TemperaturesAdapter adapter = new TemperaturesAdapter(history);
        recyclerView.setAdapter(adapter);
    }
}

