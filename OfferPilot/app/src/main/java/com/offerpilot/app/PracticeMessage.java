package com.offerpilot.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "practice_messages")
public class PracticeMessage {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long sessionId;
    private String content;
    private String sender;
    private long createdAt;

    public PracticeMessage(
            long sessionId,
            String content,
            String sender,
            long createdAt) {

        this.sessionId = sessionId;
        this.content = content;
        this.sender = sender;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
