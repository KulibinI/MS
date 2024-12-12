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

public class FragmentUpdate extends Fragment {

    private NotesDatabaseHelper dbHelper;
    private EditText editTextId, editTextDescription;
    private OnNoteUpdatedListener listener; // Интерфейс для уведомления

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteUpdatedListener) {
            listener = (OnNoteUpdatedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNoteUpdatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        editTextId = view.findViewById(R.id.editTextNoteId);
        editTextDescription = view.findViewById(R.id.editTextNoteDescription);
        Button updateButton = view.findViewById(R.id.updateButton);

        dbHelper = new NotesDatabaseHelper(getContext());

        updateButton.setOnClickListener(v -> {
            String idString = editTextId.getText().toString().trim();
            String newDescription = editTextDescription.getText().toString().trim();

            if (!idString.isEmpty() && !newDescription.isEmpty()) {
                int noteId = Integer.parseInt(idString);
                dbHelper.updateNote(noteId, newDescription);
                Toast.makeText(getContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
                editTextDescription.setText("");

                // Уведомляем слушателя об обновлении заметки
                if (listener != null) {
                    listener.onNoteUpdated();
                }
            } else {
                Toast.makeText(getContext(), "Введите ID и новое описание заметки", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}