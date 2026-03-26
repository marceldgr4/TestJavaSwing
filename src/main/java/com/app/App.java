package com.app;

import com.app.UI.MainFrame;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }catch (Exception ignored){}
            new MainFrame().setVisible(true);
        });
    }
}
