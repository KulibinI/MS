package com.example.myapplication13;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText journalNumberInput;
    private Button downloadButton, viewButton, deleteButton;
    private File downloadDirectory;
    private File downloadedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        journalNumberInput = findViewById(R.id.journalNumberInput);
        downloadButton = findViewById(R.id.downloadButton);
        viewButton = findViewById(R.id.viewButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Создаем папку для загрузок при первом запуске
        downloadDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Journals");
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }

        downloadButton.setOnClickListener(view -> {
            String journalNumber = journalNumberInput.getText().toString();
            if (!journalNumber.isEmpty()) {
                String downloadUrl = "https://ntv.ifmo.ru/file/journal/" + journalNumber + ".pdf";
                downloadFile(downloadUrl);
            } else {
                Toast.makeText(MainActivity.this, "Введите номер журнала", Toast.LENGTH_SHORT).show();
            }
        });

        viewButton.setOnClickListener(view -> openFile());

        deleteButton.setOnClickListener(view -> deleteFile());
    }

    private void downloadFile(String fileUrl) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(fileUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d("DownloadStatus", "Код ответа: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    String fileName = "journal_" + journalNumberInput.getText().toString() + ".pdf";
                    downloadedFile = new File(downloadDirectory, fileName);

                    try (FileOutputStream outputStream = new FileOutputStream(downloadedFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Файл успешно загружен", Toast.LENGTH_SHORT).show();
                            viewButton.setVisibility(View.VISIBLE);
                            deleteButton.setVisibility(View.VISIBLE);
                        });
                    }
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Журнал с таким номером не найден", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("DownloadError", "Ошибка: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        Log.e("StreamError", "Ошибка при закрытии потока ввода: " + e.getMessage());
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void openFile() {
        if (downloadedFile != null && downloadedFile.exists()) {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", downloadedFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Создаем chooser для выбора приложения, которое может открыть PDF
            Intent chooser = Intent.createChooser(intent, "Выберите приложение для открытия PDF");

            try {
                startActivity(chooser);
            } catch (Exception e) {
                Toast.makeText(this, "Для открытия PDF требуется установленное приложение", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteFile() {
        if (downloadedFile.exists()) {
            if (downloadedFile.delete()) {
                Toast.makeText(this, "Файл удален", Toast.LENGTH_SHORT).show();
                viewButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Ошибка при удалении файла", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
        }
    }
}
