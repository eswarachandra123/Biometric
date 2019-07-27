package com.biometric;

public class MyResult {
    public byte[] skey;
    public String message;

    public MyResult(byte[] skey, String message) {
        this.skey = skey;
        this.message = message;
    }

    public byte[] getskey() {
        return skey;
    }

    public String getmessage() {
        return message;
    }
}