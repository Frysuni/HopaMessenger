package com.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sun.applet.Main;

public class HopaMessengerMessageHandler implements ClientMessageHandler {

    @Override
    public void onConnection(JSONObject json) {
        String username = (String) json.get("username");
        String time = MainApp.getTimeFromStamp((Long) json.get("timestamp"));

        MainApp.addOnlineMember(username);

        System.out.printf("%s %s joined the chat.%n", time, username);
        MainApp.createNewChatMessage(time + " " + username + " присоединился к чату!", "");
    }

    @Override
    public void onDisconnection(JSONObject json) {
        String username = (String) json.get("username");
        String time = MainApp.getTimeFromStamp((Long) json.get("timestamp"));

        MainApp.removeOnlineMember(username);

        System.out.printf("%s %s disconnected from chat.%n", time, username);
        MainApp.createNewChatMessage(time + " " + username + " вышел из чата.", "");
    }

    @Override
    public void onMap(JSONObject json) {
        MainApp.enableChatScreen();

        JSONArray jsonMembers = (JSONArray) json.get("authedmembers");
        Object[] objMembers = jsonMembers.toArray();
        String[] members = new String[objMembers.length];
        for(int i = 0; i < members.length; i++) {
            members[i] = (String) objMembers[i];
        }

        MainApp.addOnlineMembers(members);
    }

    @Override
    public void onMessage(JSONObject json) {
        String time = MainApp.getTimeFromStamp((Long)json.get("timestamp"));
        String author = (String) json.get("author");
        String content = (String) json.get("content");

        System.out.printf("%s: %s\n%s%n", author, content, time);
        MainApp.createNewChatMessage(time + " " + author + ": ", content);
    }
}
