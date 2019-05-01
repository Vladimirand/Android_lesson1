package com.nehvedovich.vladimir.pogoda.screens.utils;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.database.City;
import com.nehvedovich.vladimir.pogoda.screens.database.CityRepository;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CitiesFragment;

import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.RecyclerListViewHolder> {

    private final List<City> list;

    private final CitiesFragment.OnItemClickListener listener;
    private static int selectedPosition = -1;
    private static City selectedCity = null;

    public RecyclerListAdapter(List<City> list, CitiesFragment.OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_recycler_view, viewGroup, false);
        TextView textView = view.findViewById(R.id.city_name);
        textView.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.item_selector));
        return new RecyclerListViewHolder(view, list, listener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListViewHolder recyclerListViewHolder, int i) {
        recyclerListViewHolder.textView.setText(list.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class RecyclerListViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        View view;

        RecyclerListViewHolder(@NonNull final View itemView, @NonNull final List<City> list, final CitiesFragment.OnItemClickListener listener, @NonNull final RecyclerListAdapter listAdapter) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.city_name);
            view = itemView;
            registerLongClickListener(list, listAdapter);
            registerClickListener(itemView, list, listener);
        }

        private void registerClickListener(@NonNull final View itemView, @NonNull final List<City> list, final CitiesFragment.OnItemClickListener listener) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getLayoutPosition();
                    selectedCity = list.get(selectedPosition);
                    listener.itemClicked(selectedCity);
                    itemView.setPressed(true);
                }
            });
        }

        private void registerLongClickListener(@NonNull final List<City> list, @NonNull final RecyclerListAdapter listAdapter) {
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    TextView textView = (TextView) v;
                    String messageText = v.getResources().getString(R.string.confirmDelete);
                    messageText = String.format(messageText, textView.getText());
                    builder.setTitle(R.string.delete)
                            .setMessage(messageText)

                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })

                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int index = getLayoutPosition();
                                    City city = list.get(index);
                                    CityRepository.getInstance().delete(city);
                                    list.remove(index);
                                    listAdapter.notifyDataSetChanged();
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });
        }
    }
}