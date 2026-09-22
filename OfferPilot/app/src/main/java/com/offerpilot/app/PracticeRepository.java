package com.offerpilot.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

public class PracticeRepository {

    public interface SessionCallback {
        void onSessionCreated(long sessionId);
    }

    public interface MessagesCallback {
        void onMessagesLoaded(List<PracticeMessage> messages);
    }

    private final AppDatabase database;
    private final ExecutorService databaseExecutor;
    private final Handler mainHandler;

    public PracticeRepository(Context context) {
        database = AppDatabase.getInstance(context);
        databaseExecutor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void createSession(
            String practiceMode,
            int totalQuestionCount,
            SessionCallback callback) {

        long now = System.currentTimeMillis();

        PracticeSession session = new PracticeSession(
                practiceMode,
                "IN_PROGRESS",
                1,
                now,
                now
        );

        databaseExecutor.execute(() -> {
            long sessionId = database
                    .practiceSessionDao()
                    .insert(session);

            mainHandler.post(() -> {
                callback.onSessionCreated(sessionId);
            });
        });
    }

    public void saveMessage(
            long sessionId,
            String content,
            String sender) {

        PracticeMessage message = new PracticeMessage(
                sessionId,
                content,
                sender,
                System.currentTimeMillis()
        );

        databaseExecutor.execute(() -> {
            database
                    .practiceMessageDao()
                    .insert(message);
        });
    }

    public void updateProgress(
            long sessionId,
            int questionIndex,
            String status) {

        databaseExecutor.execute(() -> {
            database
                    .practiceSessionDao()
                    .updateProgress(
                            sessionId,
                            questionIndex,
                            status,
                            System.currentTimeMillis()
                    );
        });
    }

    public void getMessages(
            long sessionId,
            MessagesCallback callback) {

        databaseExecutor.execute(() -> {
            List<PracticeMessage> messages =
                    database
                            .practiceMessageDao()
                            .getMessagesBySessionId(sessionId);

            mainHandler.post(() -> {
                callback.onMessagesLoaded(messages);
            });
        });
    }

    public void shutdown() {
        databaseExecutor.shutdown();
    }
}
