package com.offerpilot.app;

public class AiResponse {

    private final String message;
    private final String action;
    private final boolean questionFinished;

    public AiResponse(
            String message,
            String action,
            boolean questionFinished) {

        this.message = message;
        this.action = action;
        this.questionFinished = questionFinished;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public boolean isQuestionFinished() {
        return questionFinished;
    }
}
