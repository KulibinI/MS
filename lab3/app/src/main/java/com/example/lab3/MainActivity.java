package com.example.lab3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView gestureText;
    private GestureDetector gestureDetector;
    private Button button2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureText = findViewById(R.id.gestureText);
        gestureDetector = new GestureDetector(this, new GestureListener());
        button2 = findViewById(R.id.button2);

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialogFragment dialog = new FullScreenDialogFragment();
                dialog.show(getSupportFragmentManager(), "FullScreenDialog");
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            gestureText.setText("Касание (onDown)");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            gestureText.setText("Касание удержано (onShowPress)");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            gestureText.setText("Одиночный клик (onSingleTapUp)");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            gestureText.setText("Подтверждён одиночный клик");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            gestureText.setText("Двойной клик (onDoubleTap)");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            gestureText.setText("Событие двойного нажатия (onDoubleTapEvent)");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            gestureText.setText("Долгое нажатие (onLongPress)");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() < e2.getX()) {
                gestureText.setText("Смахивание вправо (onFling)");
            } else {
                gestureText.setText("Смахивание влево (onFling)");
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            gestureText.setText("Прокрутка (onScroll)");
            return true;
        }
    }
}