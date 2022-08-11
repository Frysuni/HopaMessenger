package com.example;

public class Message {
    public final String notEncryptedPart;
    public final String encryptedPart;

    public Message(String notEncryptedPart, String encryptedPart) {
        this.notEncryptedPart = notEncryptedPart;
        this.encryptedPart = encryptedPart;
    }

    public String getNotEncryptedPart() {
        return notEncryptedPart;
    }

    public String getEncryptedPart() {
        return encryptedPart;
    }

    public String toString(){
        return notEncryptedPart + encryptedPart;
    }
}
