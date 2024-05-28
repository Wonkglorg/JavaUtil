package com.wonkglorg.util.database.values;

public record DbPassword(String password) {
    @Override
    public String toString() {
        return password;
    }
}
