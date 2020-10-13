package com.rakpak.pak_parak_admin.Model;

public class GlobalChatData {

    private String GlobalChat, date, message, name;
    private String time, type, Uri;

    public GlobalChatData() {
    }

    public GlobalChatData(String globalChat, String date, String message, String name, String time, String type, String uri) {
        GlobalChat = globalChat;
        this.date = date;
        this.message = message;
        this.name = name;
        this.time = time;
        this.type = type;
        Uri = uri;
    }

    public String getGlobalChat() {
        return GlobalChat;
    }

    public void setGlobalChat(String globalChat) {
        GlobalChat = globalChat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }
}
