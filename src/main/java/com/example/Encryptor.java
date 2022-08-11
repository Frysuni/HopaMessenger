package com.example;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class Encryptor {
    private final String key;

    public Encryptor(String key){
        this.key = key;
    }

    public String encrypt(String strToEn){
        byte[] strToEnBuffer = strToEn.getBytes(StandardCharsets.UTF_16);
        byte[] enBuffer = new byte[strToEnBuffer.length];
        byte[] keyBuffer = HexBin.decode(key);

        new Random(keyBuffer[0]).nextBytes(enBuffer);

        for(int i = 0; i < enBuffer.length; i++){
            strToEnBuffer[i] += enBuffer[i] + keyBuffer[i % (keyBuffer.length - 1) + 1];
        }
        return HexBin.encode(strToEnBuffer);
    }

    public String decrypt(String strToDec){
        byte[] keyBuffer = HexBin.decode(key);
        byte[] strToDecBuffer = HexBin.decode(strToDec);
        byte[] decBuffer = new byte[strToDecBuffer.length];

        new Random(keyBuffer[0]).nextBytes(decBuffer);

        for(int i = 0; i < decBuffer.length; i++){
            strToDecBuffer[i] -= decBuffer[i] + keyBuffer[i % (keyBuffer.length - 1) + 1];
        }

        return StandardCharsets.UTF_16.decode(ByteBuffer.wrap(strToDecBuffer)).toString();
    }
}
