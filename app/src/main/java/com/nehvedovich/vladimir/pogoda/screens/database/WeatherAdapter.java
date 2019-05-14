package com.nehvedovich.vladimir.pogoda.screens.database;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.MainActivity;

// Адаптер для RecycleView
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    // Здесь нам нужен только читатель данных
    private WeatherDataReader weatherDataReader;
    // Слушатель, который будет устанавливаться извне
    private OnMenuItemClickListener itemMenuClickListener;

    public WeatherAdapter(WeatherDataReader weatherDataReader) {
        this.weatherDataReader = weatherDataReader;
    }

    // Вызывается при создании новой карточки списка
    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Создаем новый элемент пользовательского интерфейса
    // Через Inflater
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
    // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Привязываем данные к карточке
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        holder.bind(weatherDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return weatherDataReader.getCount();
    }

    // Установка слушателя
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemMenuClickListener = onMenuItemClickListener;
    }

    // Интерфейс для обработки меню
    public interface OnMenuItemClickListener {
        void onItemDeleteClick(Weather weather);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textNote;
        private Weather weather;
        private ImageView backGround;

        ViewHolder(View itemView) {
            super(itemView);

            backGround = itemView.findViewById(R.id.backgroundList);
            if (MainActivity.night) {
                backGround.setVisibility(View.VISIBLE);
            } else {
                backGround.setVisibility(View.GONE);
            }

            textNote = itemView.findViewById(R.id.textTitle);
            // При тапе на элементе – вытащим  меню
            textNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemMenuClickListener != null) {
                        showPopupMenu(textNote);
                    }
                }
            });
        }

        void bind(Weather weather) {
            this.weather = weather;
            textNote.setText(String.format("%s %s %s  (%s)", weather.time, weather.title, weather.description, weather.weatherCondition));
        }

        private void showPopupMenu(View view) {
            // Покажем меню на элементе
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                // Обработка выбора пункта меню
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // Делегируем обработку слушателю
                    if (item.getItemId() == R.id.menu_delete) {
                        itemMenuClickListener.onItemDeleteClick(weather);
                        return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }
}

