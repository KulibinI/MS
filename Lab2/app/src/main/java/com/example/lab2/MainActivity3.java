package com.example.lab2;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity3 extends AppCompatActivity {

    private final EditText[] editTexts = new EditText[6];
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        editTexts[0] = findViewById(R.id.editTextText4);
        editTexts[1] = findViewById(R.id.editTextText5);
        editTexts[2] = findViewById(R.id.editTextText6);
        editTexts[3] = findViewById(R.id.editTextText7);
        editTexts[4] = findViewById(R.id.editTextText8);
        editTexts[5] = findViewById(R.id.editTextText9);
        button4 = findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder route = new StringBuilder();
                for (EditText editText : editTexts) {
                    route.append(editText.getText().toString()).append(", ");
                }
                if (route.length() > 0) {
                    route.setLength(route.length() - 2);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("route", route.toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }
}