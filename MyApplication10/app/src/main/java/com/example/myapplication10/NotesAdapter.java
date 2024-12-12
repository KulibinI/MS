package com.example.myapplication10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {

    public NotesAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Получаем текущую заметку
        Note note = getItem(position);

        // Если конвертируемое представление пустое, создаем новое
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        // Находим элементы текстового представления
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        // Устанавливаем текст для каждого элемента
        text1.setText("Заметка #" + note.getId());
        text2.setText(note.getDescription());

        // Возвращаем заполненное представление
        return convertView;
    }
}