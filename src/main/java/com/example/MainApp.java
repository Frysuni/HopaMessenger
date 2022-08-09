package com.example;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.simple.*;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;


import javax.swing.*;
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
    final URL url = new URL("http://jsonplaceholder.typicode.com/posts?_limit=10");
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    Scanner input = new Scanner(System.in);

    public static boolean isOnLoginScreen = false;

    public static ArrayList<String> onlineMembers;

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

    static MyWebSocketClient client;
    public static String username;
    public static String password;
    public static String encryptKey;

    public static Color backgroundColor = new Color(80, 80, 100);
    public static Color contentBackgroundColor = new Color(100, 100, 120);
    public static Color buttonColor = new Color(60, 60, 80);
    public static Color foregroundColor = new Color(200, 200, 200);

    public static JFrame mainFrame = new JFrame("HopaMessenger");

    public static ConnectionSicrle loginConnectionSicrle = new ConnectionSicrle();

    public static JPanel loginPanel = new JPanel();
    public static JLabel loginErrorLabel = new JLabel();
    static {
        loginPanel.setLayout(null);
        loginPanel.setPreferredSize(new Dimension(500, 300));
        loginPanel.setBounds(0, 0, 500, 300);

        JLabel fryshostLabel = new JLabel("Введите данные учётной записи FrysHost");
        JLabel usernameLabel = new JLabel("Ник: ");
        JLabel passwordLabel = new JLabel("Пароль: ");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(action -> {
            if(client.isOpen()) {
                if(usernameField.getText().equals("") || new String(passwordField.getPassword()).equals("")){
                    loginErrorLabel.setText("Поля ввода не могут быть пустыми");
                } else {
                    JSONObject json = new JSONObject();
                    json.put("username", usernameField.getText());
                    json.put("password", new String(passwordField.getPassword()));
                    client.send("auth " + json.toJSONString());
                    password = new String(passwordField.getPassword());
                    username = usernameField.getText();
                    loginErrorLabel.setText("");
                }
            } else {
                loginErrorLabel.setText("Нет подключения к серверу");
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
        loginConnectionSicrle.setBounds(470, 10, 20, 20);

        loginPanel.add(fryshostLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordField);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(loginButton);
        loginPanel.add(loginErrorLabel);
        loginPanel.add(loginConnectionSicrle);
    }


    public static JPanel chatPanel = new JPanel();
    public static JEditorPane chatContentPane = new JEditorPane();
    public static JEditorPane onlineMembersPane = new JEditorPane();
    static {
        chatPanel.setLayout(null);
        chatPanel.setPreferredSize(new Dimension(800, 600));
        chatPanel.setBounds(0, 0, 800, 600);

        JScrollPane scrollPane = new JScrollPane(chatContentPane);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        JButton sendButton = new JButton("Отправить");
        JTextArea inputArea = new JTextArea();
        JTextField keyField = new JTextField();
        JLabel keyLabel = new JLabel("Ключ:");
        JLabel inputHereLabel = new JLabel("Написать в чат");
        JScrollPane onlineMembersScrollPane = new JScrollPane(onlineMembersPane);

        chatPanel.setBackground(backgroundColor);
        onlineMembersPane.setBackground(contentBackgroundColor);
        inputArea.setBackground(contentBackgroundColor);
        keyField.setBackground(contentBackgroundColor);
        chatContentPane.setBackground(contentBackgroundColor);
        sendButton.setBackground(buttonColor);

        onlineMembersPane.setForeground(foregroundColor);
        chatContentPane.setForeground(foregroundColor);
        inputArea.setForeground(foregroundColor);
        keyField.setForeground(foregroundColor);
        keyLabel.setForeground(foregroundColor);
        sendButton.setForeground(foregroundColor);
        loginErrorLabel.setForeground(foregroundColor);

        onlineMembersScrollPane.setBorder(null);
        keyField.setBorder(null);
        chatContentPane.setBorder(null);
        scrollPane.setBorder(null);
        sendButton.setBorderPainted(false);
        sendButton.setFocusable(false);

        chatContentPane.setEditable(false);
        onlineMembersPane.setEditable(false);
        onlineMembersPane.setPreferredSize(new Dimension(95, 400));
        scrollBar.setUnitIncrement(10);
        scrollPane.remove(scrollPane.getHorizontalScrollBar());
        sendButton.addActionListener(action -> {
            String message = inputArea.getText();
            while(message.endsWith("\n") && message.length() > 0){
                int index = message.lastIndexOf("\n");
                message = message.substring(0, index);
            }
            if(!message.equals("")) {
                client.sendMessage(message);
                inputArea.setText("");
                inputHereLabel.setVisible(true);
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
        inputHereLabel.setBounds(25, 20, 100, 25);

        chatPanel.add(scrollPane);
        chatPanel.add(inputArea);
        chatPanel.add(sendButton);
        chatPanel.add(keyField);
        chatPanel.add(keyLabel);
        chatPanel.add(onlineMembersScrollPane);
        inputArea.add(inputHereLabel);
    }
    public static void reloadOnlineMembers(){
        onlineMembersPane.setText("");

        for(String member : onlineMembers) {
            onlineMembersPane.setText(onlineMembersPane.getText() + member + "\n");
        }
    }


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


    public static void setOnlineMembers(String[] members) {
        onlineMembers = new ArrayList<>(Arrays.asList(members));
        Collections.sort(onlineMembers);
        reloadOnlineMembers();
    }

    public static void addOnlineMember(String member){
        onlineMembers.add(member);
        Collections.sort(onlineMembers);
        reloadOnlineMembers();
    }

    public static void removeOnlineMember(String member){
        onlineMembers.remove(member);
        reloadOnlineMembers();
    }


    public static String getTimeFromStamp(Long timeStamp){
        LocalDateTime localDateTime = new Timestamp(timeStamp).toLocalDateTime();

        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        time = "[" + time.split(":")[0] + ":" + time.split(":")[1] + "]";

        return time;
    }

    public static void reconnect() {
        if(client != null && !client.isClosed()) {
            disconnect();
        }
        client = new MyWebSocketClient(serverUri);
        client.connect();
    }

    public static void disconnect(){
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        if(client.isOpen()) {
            client.send("disconnect " + json.toJSONString());
        }
        client.close();
    }

    public static void handleConnection(JSONObject json){
        Boolean status = (Boolean) json.get("status");
        String reason = (String) json.get("reason");
        String username = (String) json.get("username");
        String time = getTimeFromStamp((Long) json.get("timestamp"));

        if(status) {
            System.out.printf("%s has joined the chat.\n%s%n", username, time);
            chatContentPane.setText(chatContentPane.getText() + time + " " + username + " присоединился к чату!\n");
            if(Objects.equals(username, MainApp.username)) {
                enableChatScreen();
            } else {
                addOnlineMember(username);
            }
        } else {
            System.out.println("Cant log in: " + reason);
            loginErrorLabel.setText("Не удалось авторизоваться: " + reason);
        }
    }

    public static void handleDisconnection(JSONObject json){
        String username = (String) json.get("username");
        String time = getTimeFromStamp((Long) json.get("timestamp"));
        Boolean status = (Boolean) json.get("status");

        if(status) {
            System.out.printf("%s disconnected from chat.\n%s%n", username, time);
            chatContentPane.setText(chatContentPane.getText() + time + " " + username + " вышел из чата.\n");
            removeOnlineMember(username);
        }
    }

    public static void handleMessage(JSONObject json) {
        String time = getTimeFromStamp((Long)json.get("timestamp"));
        String author = (String) json.get("author");
        String content = (String) json.get("content");

        System.out.printf("%s: %s\n%s%n", author, content, time);
        chatContentPane.setText(chatContentPane.getText() + time + " " + author + ": " + content + "\n");
    }

    public static void handleMap(JSONObject json) {
        JSONArray jsonMembers = (JSONArray) json.get("authedmembers");
        String key = (String) json.get("key");
        MainApp.encryptKey = key;

        Object[] objMembers = jsonMembers.toArray();
        String[] members = new String[objMembers.length];
        for(int i = 0; i < members.length; i++) {
            members[i] = (String) objMembers[i];
        }

        setOnlineMembers(members);
    }

    public MainApp() throws Throwable {
        Runtime.getRuntime().addShutdownHook(new Thread(MainApp::disconnect));

        reconnect();

        enableLoginScreen();
        //enableChatScreen();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);

        while(true){
            String string = input.next() + input.nextLine();

            if(string.equals("EXIT")) {
                client.close();
                return;
            } else {
                try {
                    client.sendMessage(string);
                } catch (WebsocketNotConnectedException ex) {
                    System.out.println("Connection lost! Try to reconnect.");
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        new MainApp();
    }
}
