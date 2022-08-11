package com.example;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class LoginScreen extends JFrame {
    private JPanel rootPanel;
    private JButton loginButton;
    private JTextField textField1;
    private JPasswordField passwordField1;

    public LoginScreen() {
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(rootPanel.getBackground());
        setUndecorated(true);
        pack();
    }

}
