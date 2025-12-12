package com.aurora.wave.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aurora.wave.data.entity.ReactionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReactionDao_Impl implements ReactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReactionEntity> __insertionAdapterOfReactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllByUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMessageId;

  public ReactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReactionEntity = new EntityInsertionAdapter<ReactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reactions` (`messageId`,`userId`,`emoji`,`userName`,`createdAt`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReactionEntity entity) {
        statement.bindString(1, entity.getMessageId());
        statement.bindString(2, entity.getUserId());
        statement.bindString(3, entity.getEmoji());
        if (entity.getUserName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUserName());
        }
        statement.bindLong(5, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reactions WHERE messageId = ? AND userId = ? AND emoji = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllByUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reactions WHERE messageId = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByMessageId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reactions WHERE messageId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ReactionEntity reaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReactionEntity.insert(reaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<ReactionEntity> reactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReactionEntity.insert(reactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String messageId, final String userId, final String emoji,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, messageId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, userId);
        _argIndex = 3;
        _stmt.bindString(_argIndex, emoji);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllByUser(final String messageId, final String userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllByUser.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, messageId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllByUser.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByMessageId(final String messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByMessageId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, messageId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByMessageId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReactionEntity>> getByMessageId(final String messageId) {
    final String _sql = "SELECT * FROM reactions WHERE messageId = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reactions"}, new Callable<List<ReactionEntity>>() {
      @Override
      @NonNull
      public List<ReactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReactionEntity> _result = new ArrayList<ReactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReactionEntity _item;
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ReactionEntity(_tmpMessageId,_tmpUserId,_tmpEmoji,_tmpUserName,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByMessageIdAndEmoji(final String messageId, final String emoji,
      final Continuation<? super List<ReactionEntity>> $completion) {
    final String _sql = "SELECT * FROM reactions WHERE messageId = ? AND emoji = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    _argIndex = 2;
    _statement.bindString(_argIndex, emoji);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReactionEntity>>() {
      @Override
      @NonNull
      public List<ReactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReactionEntity> _result = new ArrayList<ReactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReactionEntity _item;
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ReactionEntity(_tmpMessageId,_tmpUserId,_tmpEmoji,_tmpUserName,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByMessageIdAndUserId(final String messageId, final String userId,
      final Continuation<? super List<ReactionEntity>> $completion) {
    final String _sql = "SELECT * FROM reactions WHERE messageId = ? AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    _argIndex = 2;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReactionEntity>>() {
      @Override
      @NonNull
      public List<ReactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReactionEntity> _result = new ArrayList<ReactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReactionEntity _item;
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ReactionEntity(_tmpMessageId,_tmpUserId,_tmpEmoji,_tmpUserName,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEmojisByMessageId(final String messageId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT emoji FROM reactions WHERE messageId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReactionCount(final String messageId, final String emoji,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM reactions WHERE messageId = ? AND emoji = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    _argIndex = 2;
    _statement.bindString(_argIndex, emoji);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
