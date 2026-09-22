package com.offerpilot.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PracticeSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PracticeSession session);

    @Update
    void update(PracticeSession session);

    @Query("SELECT * FROM practice_sessions ORDER BY updatedAt DESC")
    List<PracticeSession> getAllByUpdatedTime();

    @Query("SELECT * FROM practice_sessions WHERE id = :sessionId LIMIT 1")
    PracticeSession getById(long sessionId);

    @Query("UPDATE practice_sessions "
            + "SET currentQuestionIndex = :questionIndex, "
            + "status = :status, "
            + "updatedAt = :updatedAt "
            + "WHERE id = :sessionId")
    void updateProgress(
            long sessionId,
            int questionIndex,
            String status,
            long updatedAt
    );
}
