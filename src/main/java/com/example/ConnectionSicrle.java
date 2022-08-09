package com.example;

import javax.swing.*;
import java.awt.*;

public class ConnectionSicrle extends JComponent {
    private Color color;

    public ConnectionSicrle() {
        disconnected();
    }

    public void connected(){
        color = new Color(0, 200, 0);
        repaint();
    }
    public void disconnected() {
        color = new Color(200, 0, 0);
        repaint();
    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.fillOval(0, 0, 20, 20);
    }
}
