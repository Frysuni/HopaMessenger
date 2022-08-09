package com.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.util.Map;

public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected");
        MainApp.loginConnectionSicrle.connected();
    }

    @Override
    public void onMessage(String s) {
        if(s.startsWith("auth ")){
            s = s.replaceFirst("auth ", "");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null) {
                MainApp.handleConnection(json);
            }
        } else if(s.startsWith("message ")){
            s = s.replaceFirst("message ", "");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                MainApp.handleMessage(json);
            }
        } else if(s.startsWith("disconnect ")) {
            s = s.replaceFirst("disconnect ", "");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                MainApp.handleDisconnection(json);
            }
        } else if(s.startsWith("map ")) {
            s = s.replaceFirst("map ", "");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null){
                MainApp.handleMap(json);
            }
        } else if(s.startsWith("{")){
            System.out.println("Missing prefix: " + s);
        } else {
            System.out.println("Incorrect prefix received: " + s);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed");
        MainApp.loginConnectionSicrle.disconnected();
        MainApp.enableLoginScreen();
        MainApp.reconnect();
    }

    @Override
    public void onError(Exception e) {
        System.out.println("ERROR: " + e.getMessage());
        e.printStackTrace();
    }

    public void sendMessage(String message){
        JSONObject json = new JSONObject();
        json.put("content", message);
        json.put("author", MainApp.username);
        send("message " + json.toJSONString());
    }
}
