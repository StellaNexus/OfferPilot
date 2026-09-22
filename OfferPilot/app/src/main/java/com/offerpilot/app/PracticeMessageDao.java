package com.offerpilot.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PracticeMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PracticeMessage message);

    @Query("SELECT * FROM practice_messages "
            + "WHERE sessionId = :sessionId "
            + "ORDER BY createdAt ASC")
    List<PracticeMessage> getMessagesBySessionId(long sessionId);

    @Query("DELETE FROM practice_messages WHERE sessionId = :sessionId")
    void deleteBySessionId(long sessionId);
}
