package com.offerpilot.app;

public class ChatMessage {

    private final String content;
    private final boolean fromUser;

    public ChatMessage(String content, boolean fromUser) {
        this.content = content;
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isFromUser() {
        return fromUser;
    }
}
