package com.example.khaledelsayed.bluetalk;

/**
 * Created by khaled el sayed on 11/24/2017.
 */

public class DataMessage {
    DataMessage(){

    }
    DataMessage(String msj,String sen){
        message = msj;
        sender = sen;
    }
    public  String message;
    public String sender;
}
