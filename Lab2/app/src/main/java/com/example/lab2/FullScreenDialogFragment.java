package com.example.lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FullScreenDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fullscreen, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Установка диалога на весь экран
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.fullscreen_text);
        textView.setText("1. Разработать приложение Taxi, состоящее из трех Activity.\n" +
                "2. В первом Activity создать три редактируемых текстовых поля (EditText) для ввода пользователем регистрационных данных (телефон, имя и фамилия) и кнопку Registration для запуска второго Activity.\n" +
                "3. При нажатии кнопки Registration выполнить явный вызов второго Activity с передачей данных о пользователе (телефон, имя и фамилия).\n" +
                "4. Во втором Activity создать два текстовых поля (TextView) для вывода переданной информации о пользователе (имя и фамилия, телефон), пустое по умолчанию текстовое поле (TextView) для вывода маршрута движения, кнопку Set path для ввода этого маршрута, кнопку вызова такси Call Taxi (недоступна, пока не введен маршрут движения).\n" +
                "5. При нажатии кнопки Set path выполнить неявный вызов третьего Activity с помощью метода startActivityForResult.\n" +
                "6. В третьем Activity создать шесть редактируемых текстовых полей (EditText) для ввода параметров маршрута движения, кнопку OК для возврата во второе Activity.\n" +
                "7. При нажатии кнопки ОК реализовать возврат во второе Activity с передачей в качестве результата параметров маршрута движения.\n" +
                "8. После возврата во второе Activity в текстовое поле вывести информацию о маршруте движения и предложение вызвать такси, кнопку вызова такси Call taxi сделать доступной.\n" +
                "9. При нажатии кнопки Call Taxi вывести всплывающее сообщение об успешной отправке такси.\n" +
                "10. Реализовать сохранение регистрационных данных пользователя в исходном Activity с помощью класса SharedPreferences и восстанавливать эту информацию при повторных запусках приложения. При этом название кнопки Registration должно программно меняться на Log in.\n");

        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());
    }
}