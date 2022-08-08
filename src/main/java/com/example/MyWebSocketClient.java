package com.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.util.Map;

public class MyWebSocketClient extends WebSocketClient {
    private final String username;
    private final String password;

    public MyWebSocketClient(URI serverUri, String username, String password) {
        super(serverUri);
        this.username = username;
        this.password = password;
    }

    public MyWebSocketClient(URI serverUri, Draft protocolDraft, String username, String password) {
        super(serverUri, protocolDraft);
        this.username = username;
        this.password = password;
    }

    public MyWebSocketClient(URI serverUri, Map<String, String> httpHeaders, String username, String password) {
        super(serverUri, httpHeaders);
        this.username = username;
        this.password = password;
    }

    public MyWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, String username, String password) {
        super(serverUri, protocolDraft, httpHeaders);
        this.username = username;
        this.password = password;
    }

    public MyWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout, String username, String password) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
        this.username = username;
        this.password = password;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        send("auth " + json.toJSONString());
    }

    @Override
    public void onMessage(String s) {
        if(s.startsWith("auth ")){
            s = s.replace("auth ", "");
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(s);
            } catch (Throwable e) {
                System.out.println("Corrupted json received: " + s);
            }
            if(json != null) {
                Boolean status = (Boolean) json.get("status");
                MainApp.handleConnection(json);
            }
        } else if(s.startsWith("message ")){
            s = s.replace("message ", "");
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
            s = s.replace("disconnect ", "");
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
        } else if(s.startsWith("{")){
            System.out.println("Missing prefix: " + s);
        } else {
            System.out.println("Incorrect prefix received: " + s);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("ERROR:");
        e.printStackTrace();
    }

    public void sendMessage(String message){
        JSONObject json = new JSONObject();
        json.put("content", message);
        json.put("author", username);
        send("message " + json.toJSONString());
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
