package com.app.Service;

import com.app.Model.Task;
import com.app.Model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    public Task addTask(String title, String description, TaskStatus status) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        Task task = new Task(Task.nextId(), title.trim(), description, status);
        tasks.add(task);
        return task;
    }
//--ELIMINAR
    public void deleteTask(int taskId) {
        boolean removed = tasks.removeIf(task -> task.getId() == taskId);
        if (!removed) {
            throw new IllegalArgumentException("Tarea no encontrada: " + taskId);
        }
    }
//--CAMBIAR
    public void changeTaskStatus(int taskId, TaskStatus newStatus) {
        findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + taskId))
                .setStatus(newStatus);
    }

    private Optional<Task> findById(int taskId) {
        return tasks.stream().filter(task -> task.getId() == taskId).findFirst();
    }

    public List<Task> getTasks(TaskStatus statusFilter) {
        if (statusFilter == null) {
            return new ArrayList<>(tasks);
        }
        return tasks.stream()
                .filter(task -> task.getStatus() == statusFilter)
                .collect(Collectors.toList());
    }

    public int getTotalCount() {
        return tasks.size();
    }
}