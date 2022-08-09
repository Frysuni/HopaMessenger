package com.example;

import javax.swing.*;
import java.awt.*;

public class Message extends JTextPane {
    String author;
    String content;
    String time;

    public Message (String author, String content, String time){
        super();

        this.author = author;
        this.content = content;
        this.time = time;

        setMinimumSize(new Dimension(700, 25));
        setPreferredSize(new Dimension(700, 25));
        setMaximumSize(new Dimension(700, 1000));
        setText(author + ": " + content);
        setBounds(0, 0, 700, getSize().height);
        setOpaque(false);
        setEditable(false);


        JLabel timeLabel = new JLabel(time);

        timeLabel.setBounds(600, 0, 100, 25);
        timeLabel.setPreferredSize(new Dimension(100, 25));

        add(timeLabel);
    }
}
