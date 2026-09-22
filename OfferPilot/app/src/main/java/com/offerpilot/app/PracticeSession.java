package com.offerpilot.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "practice_sessions")
public class PracticeSession {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String practiceMode;
    private String status;
    private int currentQuestionIndex;
    private long createdAt;
    private long updatedAt;

    public PracticeSession(
            String practiceMode,
            String status,
            int currentQuestionIndex,
            long createdAt,
            long updatedAt) {

        this.practiceMode = practiceMode;
        this.status = status;
        this.currentQuestionIndex = currentQuestionIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPracticeMode() {
        return practiceMode;
    }

    public String getStatus() {
        return status;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}
