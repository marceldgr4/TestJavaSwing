package com.app.UI;

import com.app.Model.Task;
import com.app.Model.TaskStatus;
import com.app.Service.TaskService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskTablePanel extends JPanel {

    private final TaskService taskService;

    private final JTable taskTable;
    private final DefaultTableModel tableModel;
    private final List<Integer> rowTaskId = new ArrayList<>();

    private final JComboBox<String> filterCombo;

    private static final String FILTER_ALL = "Todas";

    public TaskTablePanel(TaskService taskService) {
        this.taskService = taskService;


        tableModel = new DefaultTableModel(new String[]{"Id", "Título", "Descripción","Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setReorderingAllowed(false);

        filterCombo = new JComboBox<>(buildFilterOptions());
        buildUI();
        refreshTable();
    }

    private String[] buildFilterOptions() {
        TaskStatus[] statuses = TaskStatus.values();
        String[] options = new String[statuses.length + 1];
        options[0] = FILTER_ALL;
        for (int i = 0; i < statuses.length; i++) {
            options[i + 1] = statuses[i].getDisplayName();
        }
        return options;
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Lista de Tareas"));

        // ---- Toolbar: filtro + acciones ----
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.add(new JLabel("Filtrar por estado:"));

        filterCombo.setPreferredSize(new Dimension(180, 28));
        toolbar.add(filterCombo);
        filterCombo.addActionListener(e -> refreshTable());

        toolbar.add(Box.createHorizontalStrut(16));

        JButton deleteButton = createActionButton("Eliminar");
        JButton changeStatusButton = createActionButton("Cambiar Estado");

        deleteButton.addActionListener(e -> handleDeleteTask());
        changeStatusButton.addActionListener(e -> handleChangeStatus());

        toolbar.add(deleteButton);
        toolbar.add(changeStatusButton);

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
    }

    void refreshTable() {
        TaskStatus filter = resolveStatusFilter();
        List<Task> tasks = taskService.getTasks(filter);
        tableModel.setRowCount(0);
        rowTaskId.clear();

        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus()
            });
            rowTaskId.add(task.getId());
        }
    }



    private TaskStatus resolveStatusFilter() {
        String selected = (String) filterCombo.getSelectedItem();
        if (FILTER_ALL.equals(selected)) return null;
        for (TaskStatus s : TaskStatus.values()) {
            if (s.getDisplayName().equals(selected)) return s;
        }
        return null;
    }

    private void handleDeleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            showNoSelectionWarning();
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar esta tarea?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        int taskId = rowTaskId.get(selectedRow);
        try {
            taskService.deleteTask(taskId);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }

    private void handleChangeStatus() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            showNoSelectionWarning();
            return;
        }
        TaskStatus newStatus = (TaskStatus) JOptionPane.showInputDialog(
                this,
                "Seleccionar el nuevo estado:",
                "Cambiar Estado",
                JOptionPane.PLAIN_MESSAGE,
                null,
                TaskStatus.values(),
                TaskStatus.COMPLETED
        );

        if (newStatus != null) {
            int taskId = rowTaskId.get(selectedRow);
            try {
                taskService.changeTaskStatus(taskId, newStatus);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
    }

    private void showNoSelectionWarning() {
        JOptionPane.showMessageDialog(this,
                "Por favor, selecciona una tarea primero.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setFont(button.getFont().deriveFont(Font.PLAIN));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return button;
    }


}