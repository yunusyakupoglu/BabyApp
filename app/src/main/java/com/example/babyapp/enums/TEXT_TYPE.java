package com.example.babyapp.enums;

public enum TEXT_TYPE {
    COMMENT(0),
    QUESTION(1);

    private final int key;

    TEXT_TYPE(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
