package com.example.myapplication10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.content.Context;

public class FragmentAdd extends Fragment {

    private NotesDatabaseHelper dbHelper;
    private OnNoteAddedListener listener; // Интерфейс для уведомления

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteAddedListener) {
            listener = (OnNoteAddedListener) context; // Инициализация слушателя
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNoteAddedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        EditText editText = view.findViewById(R.id.editText);
        Button addButton = view.findViewById(R.id.addButton);

        dbHelper = new NotesDatabaseHelper(getContext()); // Инициализация базы данных

        addButton.setOnClickListener(v -> {
            String noteDescription = editText.getText().toString().trim(); // Получаем текст

            if (!noteDescription.isEmpty()) {
                // Добавляем заметку в базу данных
                long id = dbHelper.insertNote(noteDescription);

                if (id != -1) {
                    Toast.makeText(getContext(), "Заметка добавлена", Toast.LENGTH_SHORT).show();
                    editText.setText(""); // Очищаем поле

                    // Уведомляем слушателя об добавлении заметки
                    if (listener != null) {
                        listener.onNoteAdded();
                    }
                } else {
                    Toast.makeText(getContext(), "Ошибка при добавлении заметки", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Введите описание заметки", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}