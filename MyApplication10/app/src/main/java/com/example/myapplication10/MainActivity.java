package com.example.myapplication10;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnNoteAddedListener, OnNoteDeletedListener, OnNoteUpdatedListener {
    private static final int REQUEST_CODE = 100;
    private static final String FILE_PATH = "/storage/emulated/0/Documents/notes_backup.db";
    private ViewPager2 viewPager;
    private FragmentShow fragmentShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(this));

        // Request permission to write to external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            saveFileToSpecificLocation(FILE_PATH); // Call the method
        }
    }

    @Override
    public void onNoteAdded() {
        if (fragmentShow != null) {
            fragmentShow.updateNotes();
        }
        saveFileToSpecificLocation(FILE_PATH); // Save after addition
        Log.d("File Save", "File saved at path: " + FILE_PATH);
    }

    @Override
    public void onNoteDeleted() {
        if (fragmentShow != null) {
            fragmentShow.updateNotes();
        }
        saveFileToSpecificLocation(FILE_PATH); // Save after deletion
        Log.d("File Save", "File saved at path: " + FILE_PATH);
    }

    @Override
    public void onNoteUpdated() {
        if (fragmentShow != null) {
            fragmentShow.updateNotes();
        }
        saveFileToSpecificLocation(FILE_PATH); // Save after update
        Log.d("File Save", "File saved at path: " + FILE_PATH);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    fragmentShow = new FragmentShow();
                    return fragmentShow;
                case 1:
                    return new FragmentAdd();
                case 2:
                    return new FragmentDel();
                case 3:
                    return new FragmentUpdate();
                default:
                    return new FragmentShow();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveFileToSpecificLocation(FILE_PATH);
            }
        }
    }

    public void saveFileToSpecificLocation(String filePath) {
        File file = new File(filePath);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            String data = "Ваши данные для сохранения"; // Замените на актуальные данные
            outputStream.write(data.getBytes());
            Toast.makeText(this, "Файл сохранен: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Log.d("File Path", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
        }
    }


}
