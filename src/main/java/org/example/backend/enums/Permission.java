package org.example.backend.enums;

/**
 * Permissions for fine-grained access control
 */
public enum Permission {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    INVENTORY_READ("inventory:read"),
    INVENTORY_WRITE("inventory:write"),
    LOCATION_READ("location:read"),
    LOCATION_WRITE("location:write"),
    OPERATION_READ("operation:read"),
    OPERATION_WRITE("operation:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    DASHBOARD_READ("dashboard:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}