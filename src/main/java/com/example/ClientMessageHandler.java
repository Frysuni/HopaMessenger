package com.example;

import org.json.simple.JSONObject;

public interface ClientMessageHandler {
    // Executes when someone connected to the chat
    void onConnection(JSONObject json);

    // Executes when someone disconnected from chat
    void onDisconnection(JSONObject json);

    // Executes at connection
    void onMap(JSONObject json);

    // Executes when some-one sends a message
    void onMessage(JSONObject json);
}
