package com.example.lab20;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private List<GeoPoint> routePoints; // Список для хранения точек маршрута
    private Polyline routePolyline; // Полилиния для отображения маршрута
    private Marker currentMarker; // Ссылка на текущий маркер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация конфигурации OSMDroid
        Configuration.getInstance().load(this, android.preference.PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Инициализация FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Настройка LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Интервал обновления 10 секунд
        locationRequest.setFastestInterval(5000); // Наибыстрая частота обновления
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Высокая точность

        // Инициализация списка для маршрута
        routePoints = new ArrayList<>();
        routePolyline = new Polyline();

        // Восстановление маршрута при запуске
        loadRoute();

        // Проверка разрешений на доступ к местоположению
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            setupMap();
        }
    }

    private void setupMap() {
        // Установка начальной позиции карты
        GeoPoint startPoint;
        if (!routePoints.isEmpty()) {
            startPoint = routePoints.get(routePoints.size() - 1); // Последняя точка маршрута
        } else {
            startPoint = new GeoPoint(52.5200, 13.4050); // Запасная точка
        }

        mapView.getController().setZoom(15);
        mapView.getController().setCenter(startPoint);

        // Создание и добавление маркера
        currentMarker = new Marker(mapView);
        currentMarker.setPosition(startPoint);
        currentMarker.setTitle("Current Location");
        currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(currentMarker);
        mapView.invalidate();

        // Проверка и запуск обновлений местоположения
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                // Обновление маршрута
                GeoPoint newLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                routePoints.add(newLocation); // Добавление новой точки в маршрут
                drawRoute(); // Вызов метода для рисования маршрута

                // Обновление маркера
                currentMarker.setPosition(newLocation); // Обновление позиции маркера
                mapView.getController().setCenter(newLocation); // Центрирование карты на новом местоположении
                mapView.invalidate();

                // Сохранение маршрута после обновления
                saveRoute(); // Сохраняем маршрут при каждом обновлении
            }
        }
    };

    private void drawRoute() {
        // Очистка старой полилинии
        mapView.getOverlays().remove(routePolyline);

        // Создание новой полилинии
        routePolyline.setPoints(routePoints);
        routePolyline.setColor(getResources().getColor(R.color.colorPrimary));
        routePolyline.setWidth(5f); // Ширина линии
        mapView.getOverlays().add(routePolyline);
        mapView.invalidate(); // Обновление карты
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Вызов суперметода

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                Log.e("MainActivity", "Location permission denied");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Важно для работы карт
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback); // Остановка обновлений местоположения
        mapView.onPause(); // Важно для работы карт
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveRoute(); // Сохранение маршрута перед уничтожением
        mapView.onDetach(); // Освобождение ресурсов
    }

    // Метод для сохранения маршрута
    private void saveRoute() {
        Log.d("MainActivity", "Saving route...");
        SharedPreferences sharedPreferences = getSharedPreferences("RoutePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (GeoPoint point : routePoints) {
            sb.append(point.getLatitude()).append(",").append(point.getLongitude()).append(";");
        }
        editor.putString("saved_route", sb.toString());
        editor.apply();
        Log.d("MainActivity", "Route to save: " + sb.toString()); // Лог для отладки
    }

    private void loadRoute() {
        Log.d("MainActivity", "Loading route...");
        SharedPreferences sharedPreferences = getSharedPreferences("RoutePrefs", MODE_PRIVATE);
        String savedRoute = sharedPreferences.getString("saved_route", "");
        Log.d("MainActivity", "Loaded route string: " + savedRoute); // Лог для проверки загруженной строки

        if (!savedRoute.isEmpty()) {
            String[] points = savedRoute.split(";");
            for (String point : points) {
                String[] coords = point.split(",");
                if (coords.length == 2) {
                    try {
                        double latitude = Double.parseDouble(coords[0]);
                        double longitude = Double.parseDouble(coords[1]);
                        routePoints.add(new GeoPoint(latitude, longitude));
                        Log.d("MainActivity", "Point loaded: " + latitude + ", " + longitude); // Лог для отладки каждой точки
                    } catch (NumberFormatException e) {
                        Log.e("MainActivity", "Error parsing coordinates: " + point, e);
                    }
                }
            }
            drawRoute(); // Рисуем загруженный маршрут
            Log.d("MainActivity", "Route loaded successfully."); // Лог для отладки
        } else {
            Log.d("MainActivity", "No saved route found."); // Лог для отладки
        }
    }
}
