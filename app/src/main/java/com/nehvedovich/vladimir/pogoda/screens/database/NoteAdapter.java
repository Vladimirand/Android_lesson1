package com.nehvedovich.vladimir.pogoda.screens.database;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;

// Адаптер для RecycleView
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    // Здесь нам нужен только читатель данных
    private NoteDataReader noteDataReader;
    // Слушатель, который будет устанавливаться извне
    private OnMenuItemClickListener itemMenuClickListener;

    public NoteAdapter(NoteDataReader noteDataReader) {
        this.noteDataReader = noteDataReader;
    }

    // Вызывается при создании новой карточки списка
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// Создаем новый элемент пользовательского интерфейса
// Через Inflater
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
// Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Привязываем данные к карточке
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        holder.bind(noteDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return noteDataReader.getCount();
    }

    // Установка слушателя
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemMenuClickListener = onMenuItemClickListener;
    }

    // Интерфейс для обработки меню
    public interface OnMenuItemClickListener {
        void onItemDeleteClick(Note note);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textNote;
        private Note note;

        ViewHolder(View itemView) {
            super(itemView);
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

        void bind(Note note) {
            this.note = note;
            textNote.setText(String.format("%s %s %s  (%s)", note.time, note.title, note.description, note.weatherCondition));
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
                        itemMenuClickListener.onItemDeleteClick(note);
                        return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }
}

