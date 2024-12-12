package com.example.myapplication10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentShow extends Fragment {

    private NotesDatabaseHelper dbHelper;
    private NotesAdapter adapter;
    private List<Note> noteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ListView listView = view.findViewById(R.id.listView);

        // Инициализация базы данных
        dbHelper = new NotesDatabaseHelper(getContext());

        // Получение всех заметок из базы данных
        noteList = dbHelper.getAllNotes();

        // Инициализация адаптера и установка его в ListView
        adapter = new NotesAdapter(getContext(), noteList);
        listView.setAdapter(adapter);

        return view;
    }

    // Метод для обновления списка заметок
    public void updateNotes() {
        noteList.clear();
        noteList.addAll(dbHelper.getAllNotes()); // Получаем актуальные заметки из базы данных
        adapter.notifyDataSetChanged(); // Уведомление адаптера об изменениях
    }
}