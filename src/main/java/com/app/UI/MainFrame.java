package com.app.UI;

import com.app.Service.TaskService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

   public MainFrame() {
      setTitle("TO-DO APP");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setPreferredSize(new Dimension(600, 550));
      setMinimumSize(new Dimension(600, 430));
      setResizable(true);
      setLocationRelativeTo(null);

      TaskService taskService = new TaskService();

      TaskTablePanel tablePanel = new TaskTablePanel(taskService);
      TaskFormPanel formPanel = new TaskFormPanel(taskService, unused -> tablePanel.refreshTable());

      setLayout(new BorderLayout(10, 10));
      getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      add(formPanel, BorderLayout.NORTH);
      add(tablePanel, BorderLayout.CENTER);

      pack();
   }
}