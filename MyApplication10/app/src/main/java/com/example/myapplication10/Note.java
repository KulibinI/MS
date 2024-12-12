package com.example.myapplication10;

public class Note {
    private int id;
    private String description;

    // Конструктор
    public Note(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // Геттер для id
    public int getId() {
        return id;
    }

    // Геттер для description
    public String getDescription() {
        return description;
    }
}