package com.app.Model;

public enum TaskStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En Proceso"),
    COMPLETED("Completado");

    private  final String displayName;
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }

}
