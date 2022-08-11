package com.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.applet.Main;

import java.net.URI;
import java.util.Map;

public class MyWebSocketClient extends WebSocketClient {

    static final JSONParser parser = new JSONParser();

    ClientMessageHandler handler;

    public MyWebSocketClient(URI serverUri, ClientMessageHandler handler) {
        super(serverUri);
        this.handler = handler;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        MainApp.isConnecting = false;
        System.out.println("Connected");
        MainApp.loginErrorLabel.setText("Подключено");
        MainApp.isConnecting = false;
    }

    @Override
    public void onMessage(String s) {
        if(s.startsWith("message ")){
            s = s.replaceFirst("message ", "");
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                handler.onMessage(json);
            }
        }
        else if(s.startsWith("disconnected ")) {
            s = s.replaceFirst("disconnected ", "");
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                handler.onDisconnection(json);
            }
        }
        else if(s.startsWith("connected ")) {
            s = s.replaceFirst("connected ", "");
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                handler.onConnection(json);
            }
        }
        else if(s.startsWith("map ")) {
            s = s.replaceFirst("map ", "");
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                handler.onMap(json);
            }
        }
        else if(s.startsWith("{")){
            System.out.println("Missing prefix: " + s);
        }
        else {
            System.out.println("Incorrect prefix received: " + s);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed: " + s + "\nClose code: " + i + "\nBoolean: " + b);
        if(s.equals("Connection timed out: connect")) {
            MainApp.loginErrorLabel.setText("Превышено время ожидания");
        }
        else if (i == 3511) {
            MainApp.loginErrorLabel.setText("Такой логин не существует");
        }
        else if(i == 3512) {
            MainApp.loginErrorLabel.setText("Неверный пароль");
        }
        else if(i == 3513) {
            MainApp.loginErrorLabel.setText("Такой пользователь уже авторизован");
        }
        else if(i == 3510 || i == 3500) {
            MainApp.loginErrorLabel.setText("Что-то сломалось");
        }
        else {
            MainApp.loginErrorLabel.setText("Отключено");
        }
        MainApp.enableLoginScreen();

        MainApp.isConnecting = false;
    }

    @Override
    public void onError(Exception e) {
        //System.out.println(e.getMessage());
        e.printStackTrace();
    }

    public void sendMessage(String message){
        JSONObject json = new JSONObject();
        json.put("content", message);
        json.put("author", MainApp.username);
        send("message " + json.toJSONString());
    }
}
