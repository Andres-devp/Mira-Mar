package com.example.demo.enums;

public enum UserRole {
    ADMIN("Administrador"),
    OPERATOR("Operador"),
    CLIENT("Cliente");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
