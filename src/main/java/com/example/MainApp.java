package com.example;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.simple.*;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainApp {
    // Some booleans
    public static boolean isOnLoginScreen = false;
    public static boolean isConnecting = false;
    public static boolean isUsed = false;


    // Server's URI
    public static final URI serverUri;
    static {
        URI serverUri1;
        try {
            serverUri1 = new URI("ws://178.214.234.25:8553");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            serverUri1 = null;
        }
        serverUri = serverUri1;
    }


    // Main client
    static MyWebSocketClient client;


    // This user's data
    public static String username;
    public static String password;


    // Colors for UI
    public static Color backgroundColor = new Color(80, 80, 100);
    public static Color contentBackgroundColor = new Color(100, 100, 120);
    public static Color buttonColor = new Color(60, 60, 80);
    public static Color foregroundColor = new Color(200, 200, 200);


    // Main frame
    public static JFrame mainFrame = new JFrame("HopaMessenger");


    // All things login screen needs
    public static JPanel loginPanel = new JPanel();
    public static JLabel loginErrorLabel = new JLabel();
    static {
        loginPanel.setLayout(null);
        loginPanel.setPreferredSize(new Dimension(500, 300));
        loginPanel.setBounds(0, 0, 500, 300);

        JLabel fryshostLabel = new JLabel("Введите данные учётной записи FrysHost");
        JLabel usernameLabel = new JLabel("Логин: ");
        JLabel passwordLabel = new JLabel("Пароль: ");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(action -> {
            if(usernameField.getText().equals("") || new String(passwordField.getPassword()).equals("")){
                loginErrorLabel.setText("Поля ввода не могут быть пустыми");
            } else {
                client.clearHeaders();
                client.addHeader("username", usernameField.getText());
                client.addHeader("password", new String(passwordField.getPassword()));
                if(!isConnecting) {
                    if(isUsed) {
                        client.reconnect();
                    } else {
                        client.connect();
                        isUsed = true;
                    }
                    isConnecting = true;
                    password = new String(passwordField.getPassword());
                    username = usernameField.getText();
                    loginErrorLabel.setText("Подключение...");
                }
            }
        });
        /*loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(loginButton.getBounds().y == 125)
                    loginButton.setBounds(150, 225, 200, 50);
                else
                    loginButton.setBounds(150, 125, 200, 50);
            }
        });*/

        loginPanel.setBackground(backgroundColor);
        passwordField.setBackground(contentBackgroundColor);
        usernameField.setBackground(contentBackgroundColor);
        loginButton.setBackground(buttonColor);

        passwordField.setForeground(foregroundColor);
        usernameField.setForeground(foregroundColor);
        loginButton.setForeground(foregroundColor);
        fryshostLabel.setForeground(foregroundColor);
        passwordLabel.setForeground(foregroundColor);
        usernameLabel.setForeground(foregroundColor);

        passwordField.setBorder(null);
        usernameField.setBorder(null);
        loginButton.setBorderPainted(false);
        loginButton.setFocusable(false);

        fryshostLabel.setBounds(125, 10, 800, 25);
        usernameLabel.setBounds(25, 45, 75, 25);
        usernameField.setBounds(100, 45, 300, 25);
        passwordLabel.setBounds(25, 75, 75, 25);
        passwordField.setBounds(100, 75, 300, 25);
        loginButton.setBounds(150, 125, 200, 50);
        loginErrorLabel.setBounds(25, 200, 450, 25);

        loginPanel.add(fryshostLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordField);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(loginButton);
        loginPanel.add(loginErrorLabel);
    }


    // All things chat screen needs
    public static JPanel chatPanel = new JPanel();
    public static JEditorPane chatContentPane = new JEditorPane();
    public static JEditorPane onlineMembersPane = new JEditorPane();
    public static JLabel keyErrorLabel = new JLabel("");
    static {
        chatPanel.setLayout(null);
        chatPanel.setPreferredSize(new Dimension(800, 600));
        chatPanel.setBounds(0, 0, 800, 600);

        JScrollPane scrollPane = new JScrollPane(chatContentPane);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        JButton sendButton = new JButton("Отправить");
        JButton applyKeyButton = new JButton("Применить");
        JTextArea inputArea = new JTextArea();
        JTextField keyField = new JTextField();
        JLabel keyLabel = new JLabel("Ключ:");
        JLabel inputHereLabel = new JLabel("Написать в чат");
        JTextField keyStatusLabel = new JTextField("Без шифрования");
        JScrollPane onlineMembersScrollPane = new JScrollPane(onlineMembersPane);

        chatPanel.setBackground(backgroundColor);
        onlineMembersPane.setBackground(contentBackgroundColor);
        inputArea.setBackground(contentBackgroundColor);
        keyField.setBackground(contentBackgroundColor);
        chatContentPane.setBackground(contentBackgroundColor);
        sendButton.setBackground(buttonColor);
        applyKeyButton.setBackground(buttonColor);

        onlineMembersPane.setForeground(foregroundColor);
        chatContentPane.setForeground(foregroundColor);
        inputArea.setForeground(foregroundColor);
        keyField.setForeground(foregroundColor);
        keyLabel.setForeground(foregroundColor);
        sendButton.setForeground(foregroundColor);
        applyKeyButton.setForeground(foregroundColor);
        loginErrorLabel.setForeground(foregroundColor);
        keyErrorLabel.setForeground(foregroundColor);
        keyStatusLabel.setForeground(foregroundColor);

        onlineMembersScrollPane.setBorder(null);
        keyField.setBorder(null);
        chatContentPane.setBorder(null);
        scrollPane.setBorder(null);
        keyStatusLabel.setBorder(null);
        keyStatusLabel.setOpaque(false);
        keyStatusLabel.setEditable(false);
        sendButton.setBorderPainted(false);
        sendButton.setFocusable(false);
        applyKeyButton.setBorderPainted(false);
        applyKeyButton.setFocusable(false);

        chatContentPane.setEditable(false);
        onlineMembersPane.setEditable(false);
        onlineMembersPane.setPreferredSize(new Dimension(95, 400));
        scrollBar.setUnitIncrement(10);
        scrollPane.remove(scrollPane.getHorizontalScrollBar());
        sendButton.addActionListener(action -> {
            String message = inputArea.getText();
            while(message.endsWith("\n")){
                int index = message.lastIndexOf("\n");
                message = message.substring(0, index);
            }
            if(!message.equals("")) {
                client.sendMessage(message);
                inputArea.setText("");
                inputHereLabel.setVisible(true);
            }
        });
        applyKeyButton.addActionListener( action -> {
            String newKey = keyField.getText();

            if(newKey.equals("")) {
                keyErrorLabel.setText("Шифрование отключено. Обновляю чат...");
                keyErrorLabel.paintImmediately(0, 0, 490, 25);
                keyStatusLabel.setText("Без шифрования");
                encryptor = null;
                reloadChatContent();
            }
            else {
                try {
                    new Encryptor(newKey).encrypt("TEST");
                    encryptor = new Encryptor(newKey);
                    keyErrorLabel.setText("Ключ установлен. Обновляю чат...");
                    keyErrorLabel.paintImmediately(0, 0, 490, 25);
                    keyStatusLabel.setText("Шифрование ключом " + newKey);
                    reloadChatContent();
                } catch (Throwable ex) {
                    keyErrorLabel.setText("Невозможный ключ");
                }
            }
        });
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                inputHereLabel.setVisible(inputArea.getText().equals(""));
            }
        });

        inputHereLabel.disable();
        onlineMembersScrollPane.setBounds(25, 25, 100, 400);
        scrollPane.setBounds(135, 25, 640, 400);
        inputArea.setBounds(135, 450, 540, 66);
        inputArea.setPreferredSize(new Dimension(540, 66));
        sendButton.setBounds(675, 450, 100, 66);
        keyLabel.setBounds(135, 525, 50, 25);
        keyField.setBounds(185, 525, 490, 25);
        applyKeyButton.setBounds(675, 525, 100, 25);
        keyStatusLabel.setBounds(185, 550, 490, 25);
        keyErrorLabel.setBounds(185, 575, 490, 25);
        inputHereLabel.setBounds(25, 20, 100, 25);

        chatPanel.add(scrollPane);
        chatPanel.add(inputArea);
        chatPanel.add(sendButton);
        chatPanel.add(applyKeyButton);
        chatPanel.add(keyField);
        chatPanel.add(keyLabel);
        chatPanel.add(keyStatusLabel);
        chatPanel.add(keyErrorLabel);
        chatPanel.add(onlineMembersScrollPane);
        inputArea.add(inputHereLabel);
    }
    public static HashSet<String> onlineMembers = new HashSet<>();
    public static ArrayList<Message> messages = new ArrayList<>();
    public static Encryptor encryptor;
    public static void reloadOnlineMembers(){
        onlineMembersPane.setText("");

        for(String member : onlineMembers) {
            onlineMembersPane.setText(onlineMembersPane.getText() + member + "\n");
        }
    }
    public static void reloadChatContent(){
        ArrayList<Message> messages = (ArrayList<Message>) MainApp.messages.clone();
        StringBuilder out = new StringBuilder();
        if(encryptor != null) {
            for(Message message : messages){
                try {
                    String decrypted = encryptor.decrypt(message.encryptedPart);
                    out.append(message.getNotEncryptedPart()).append(decrypted);
                } catch (Throwable ex) {
                    out.append(message.getNotEncryptedPart()).append(message.getEncryptedPart());
                }
                out.append('\n');
            }
        }
        else {
            for(Message message : messages){
                out.append(message.getNotEncryptedPart()).append(message.getEncryptedPart());
                out.append('\n');
            }
        }

        chatContentPane.setText(out.toString());
        keyErrorLabel.setText("Чат успешно перешифрован");
    }
    public static void printNewChatMessage(Message message){
        StringBuilder out = new StringBuilder(chatContentPane.getText());
        if(encryptor != null) {
            try {
                String decrypted = encryptor.decrypt(message.encryptedPart);
                out.append(message.getNotEncryptedPart()).append(decrypted);
            } catch (Throwable ex) {
                out.append(message.getNotEncryptedPart()).append(message.getEncryptedPart());
            }
            out.append('\n');
        }
        else {
            out.append(message.getNotEncryptedPart()).append(message.getEncryptedPart());
            out.append('\n');
        }
        chatContentPane.setText(out.toString());
    }
    public static void createNewChatMessage(String content, String encryptedContent){
        Message message = new Message(content, encryptedContent);
        messages.add(message);

        printNewChatMessage(message);
    }


    // Switching between screens
    public static void enableChatScreen(){
        if(isOnLoginScreen) {
            mainFrame.remove(loginPanel);
            mainFrame.remove(chatPanel);
            mainFrame.add(chatPanel);
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            isOnLoginScreen = false;
        }
    }
    public static void enableLoginScreen(){
        if(!isOnLoginScreen) {
            mainFrame.remove(loginPanel);
            mainFrame.remove(chatPanel);
            mainFrame.add(loginPanel);
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            isOnLoginScreen = true;
        }
    }


    // Adding or removing online members from list
    public static void addOnlineMembers(String[] members){
        onlineMembers.addAll(Arrays.asList(members));
        reloadOnlineMembers();
    }
    public static void addOnlineMember(String member){
        onlineMembers.add(member);
        reloadOnlineMembers();
    }
    public static void removeOnlineMember(String member){
        onlineMembers.remove(member);
        reloadOnlineMembers();
    }


    // Returns time as string like "[12:00]"
    public static String getTimeFromStamp(Long timeStamp){
        LocalDateTime localDateTime = new Timestamp(timeStamp).toLocalDateTime();

        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        time = "[" + time.split(":")[0] + ":" + time.split(":")[1] + "]";

        return time;
    }


    public MainApp() {
        client = new MyWebSocketClient(serverUri, new HopaMessengerMessageHandler());

        enableLoginScreen();
        enableChatScreen();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);

        createNewChatMessage("mex312 присоеденился к чату", "");
        createNewChatMessage("mex312: ", "Это не зашифрованное сообщение");
        createNewChatMessage("mex312: ", new Encryptor("FFFF").encrypt("Это зашифрованное сообщение"));
        createNewChatMessage("mex312: ", new Encryptor("1111").encrypt("Это другое зашифрованное сообщение"));
    }

    public static void main(String[] args) throws Throwable {
        new MainApp();
    }
}
