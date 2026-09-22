package com.offerpilot.app;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionViewModel extends ViewModel {

    private final List<ChatMessage> messages = new ArrayList<>();

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public void clearMessages() {
        messages.clear();
    }
}
