package com.filmfactory.ffem.pojo;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class RecentChat implements Comparable {

    String chatWith;
    String chatWithUID;
    String date;
    String lastMessage;
    HashMap<String,Boolean> chatMessages;

    public RecentChat(){

    }

    public RecentChat(String chatWith, String chatWithUID, String date, String lastMessage, HashMap<String, Boolean> chatMessages) {
        this.chatWith = chatWith;
        this.chatWithUID = chatWithUID;
        this.date = date;
        this.lastMessage = lastMessage;
        this.chatMessages = chatMessages;
    }

    public String getChatWithUID() {
        return chatWithUID;
    }

    public void setChatWithUID(String chatWithUID) {
        this.chatWithUID = chatWithUID;
    }

    public HashMap<String, Boolean> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(HashMap<String, Boolean> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int compareTo(Object o) {

        String oDate = ((RecentChat)o).getDate();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

        try {
            Date d1 = df.parse(oDate);
            Date d2 = df.parse(this.date);
            return d1.compareTo(d2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
