package com.example.demo.domain.enums;

public enum PermissionEnum {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    LIST_ALL("LIST_ALL");

    private String name;

    PermissionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
