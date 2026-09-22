package com.offerpilot.app;

public class Question {

    private final String questionId;
    private final String content;
    private final String category;
    private final int difficulty;

    public Question(
            String questionId,
            String content,
            String category,
            int difficulty) {

        this.questionId = questionId;
        this.content = content;
        this.category = category;
        this.difficulty = difficulty;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
