package org.example.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    CREATED,
    PICKED,
    COMPLETED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static TaskStatus fromJson(String value) {
        if (value == null) {
            return null;
        }
        return TaskStatus.valueOf(value.trim().toUpperCase());
    }
}