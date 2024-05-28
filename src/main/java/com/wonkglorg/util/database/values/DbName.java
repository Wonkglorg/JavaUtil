package com.wonkglorg.util.database.values;

public record DbName(String name) {
    @Override
    public String toString() {
        return name;
    }
}
