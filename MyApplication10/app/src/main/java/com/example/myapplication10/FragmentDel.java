package com.example.myapplication10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.widget.Toast;
import android.content.Context;

public class FragmentDel extends Fragment {

    private NotesDatabaseHelper dbHelper;
    private EditText editText; // Поле для ввода ID заметки
    private OnNoteDeletedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteDeletedListener) {
            listener = (OnNoteDeletedListener) context; // Инициализация слушателя
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNoteDeletedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        editText = view.findViewById(R.id.editTextNoteId);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        dbHelper = new NotesDatabaseHelper(getContext());

        deleteButton.setOnClickListener(v -> {
            String idString = editText.getText().toString().trim();
            if (!idString.isEmpty()) {
                int noteId = Integer.parseInt(idString);
                dbHelper.deleteNote(noteId);
                Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
                editText.setText(""); // Очищаем поле

                // Уведомляем слушателя об удалении заметки
                if (listener != null) {
                    listener.onNoteDeleted();
                }
            } else {
                Toast.makeText(getContext(), "Введите ID заметки", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}