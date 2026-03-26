package com.app.UI;

import com.app.Model.TaskStatus;
import com.app.Service.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class TaskFormPanel extends JPanel {

    private final TaskService taskService;
    private final Consumer<Void> onTaskAdded;

    // -- Tamaños definidos para que los campos sean visibles
    private final JTextField titleField = new JTextField(10);
    private final JTextArea descriptionArea = new JTextArea(10, 30);
    private final JComboBox<TaskStatus> statusCombo = new JComboBox<>(TaskStatus.values());

    public TaskFormPanel(TaskService taskService, Consumer<Void> onTaskAdded) {
        this.taskService = taskService;
        this.onTaskAdded = onTaskAdded;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Nueva Tarea"),
                new EmptyBorder(8, 8, 8, 8)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== TÍTULO =====
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        // * de color rojo para indicar campo obligatorio
        JLabel titleLabel = new JLabel("Título ");
        add(titleLabel, gbc);

        JLabel required = new JLabel("*");
        required.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        JPanel titleLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        titleLabelPanel.setOpaque(false);
        JLabel star = new JLabel(" *");
        star.setForeground(Color.RED);
        titleLabelPanel.add(star);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        add(titleLabelPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        add(titleField, gbc);

        // ===== DESCRIPCIÓN =====
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(new JLabel("Descripción"), gbc);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollDesc = new JScrollPane(descriptionArea);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(scrollDesc, gbc);

        // ===== ESTADO =====
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Estado"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        statusCombo.setPreferredSize(new Dimension(200, 28));
        add(statusCombo, gbc);

        // ===== BOTÓN AGREGAR TAREA=====
        JButton addButton = new JButton("Agregar Tarea");
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        addButton.addActionListener(e -> handleAddTask());

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(addButton, gbc);
    }

    private void handleAddTask() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        TaskStatus status = (TaskStatus) statusCombo.getSelectedItem();

        try {
            taskService.addTask(title, description, status);
            clearForm();
            onTaskAdded.accept(null);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        statusCombo.setSelectedIndex(0);
        titleField.requestFocusInWindow();
    }
}