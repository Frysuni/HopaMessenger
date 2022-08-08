package com.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainApp {
    final URL url = new URL("http://jsonplaceholder.typicode.com/posts?_limit=10");
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    Scanner input = new Scanner(System.in);

    public static final URI serverUri;

    static MyWebSocketClient client;

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

    public String get(){
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while((inputLine = in.readLine()) != null){
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getTimeFromStamp(Long timeStamp){
        LocalDateTime localDateTime = new Timestamp(timeStamp).toLocalDateTime();

        String date = localDateTime.format(DateTimeFormatter.ISO_DATE);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        time = time.split(":")[0] + ":" + time.split(":")[1];

        return date + " " + time;
    }

    public static void reconnect() {
        if(client != null && !client.isClosed()) {
            disconnect();
        }
        client = new MyWebSocketClient(serverUri, "mex312", "88005553535");
        client.connect();
    }

    public static void disconnect(){
        JSONObject json = new JSONObject();
        json.put("username", client.getUsername());
        json.put("password", client.getPassword());
        client.send("disconnect " + json.toJSONString());
        client.close();
    }

    public static void handleConnection(JSONObject json){
        Boolean status = (Boolean) json.get("status");
        String reason = (String) json.get("reason");
        String username = (String) json.get("username");
        String time = getTimeFromStamp((Long) json.get("timestamp"));

        if(status) {
            System.out.printf("%s has joined the chat.\n%s%n", username, time);
        } else {
            System.out.println("Cant log in: " + reason);
            client.close();
        }
    }

    public static void handleDisconnection(JSONObject json){
        String username = (String) json.get("username");
        String time = getTimeFromStamp((Long) json.get("timestamp"));
        Boolean status = (Boolean) json.get("status");

        if(status) {
            System.out.printf("%s disconnected from chat.\n%s%n", username, time);
        }
    }

    public static void handleMessage(JSONObject json) {
        String time = getTimeFromStamp((Long)json.get("timestamp"));
        String author = (String) json.get("author");
        String content = (String) json.get("content");

        System.out.printf("%s: %s\n%s%n", author, content, time);
    }

    public MainApp() throws Throwable {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                disconnect();
            }
        }));

        /*con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(0);
        con.setReadTimeout(0);

        String jsonStr = get();
        JSONParser parser = new JSONParser();

        JSONArray json;
        json = (JSONArray) parser.parse(jsonStr);

        System.out.println(json.toJSONString());*/

        reconnect();

        while(true){
            String string = input.next() + input.nextLine();

            if(string.equals("EXIT")) {
                client.close();
                return;
            } else if(string.equals("RECONNECT")) {
                reconnect();
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
