package com.aurora.wave.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aurora.wave.data.converter.Converters;
import com.aurora.wave.data.entity.SyncStateEntity;
import com.aurora.wave.data.model.SyncStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class SyncStateDao_Impl implements SyncStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncStateEntity> __insertionAdapterOfSyncStateEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<SyncStateEntity> __updateAdapterOfSyncStateEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSyncStatus;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfMarkFailed;

  private final SharedSQLiteStatement __preparedStmtOfResetFailureCount;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByConversationId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SyncStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncStateEntity = new EntityInsertionAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sync_state` (`conversationId`,`lastSyncTimestamp`,`serverSeq`,`localSeq`,`syncStatus`,`lastSyncedMessageId`,`failureCount`,`lastError`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getConversationId());
        statement.bindLong(2, entity.getLastSyncTimestamp());
        if (entity.getServerSeq() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getServerSeq());
        }
        if (entity.getLocalSeq() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getLocalSeq());
        }
        final String _tmp = __converters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(5, _tmp);
        if (entity.getLastSyncedMessageId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastSyncedMessageId());
        }
        statement.bindLong(7, entity.getFailureCount());
        if (entity.getLastError() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLastError());
        }
        statement.bindLong(9, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfSyncStateEntity = new EntityDeletionOrUpdateAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sync_state` SET `conversationId` = ?,`lastSyncTimestamp` = ?,`serverSeq` = ?,`localSeq` = ?,`syncStatus` = ?,`lastSyncedMessageId` = ?,`failureCount` = ?,`lastError` = ?,`updatedAt` = ? WHERE `conversationId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getConversationId());
        statement.bindLong(2, entity.getLastSyncTimestamp());
        if (entity.getServerSeq() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getServerSeq());
        }
        if (entity.getLocalSeq() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getLocalSeq());
        }
        final String _tmp = __converters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(5, _tmp);
        if (entity.getLastSyncedMessageId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastSyncedMessageId());
        }
        statement.bindLong(7, entity.getFailureCount());
        if (entity.getLastError() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLastError());
        }
        statement.bindLong(9, entity.getUpdatedAt());
        statement.bindString(10, entity.getConversationId());
      }
    };
    this.__preparedStmtOfUpdateSyncStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_state SET syncStatus = ?, updatedAt = ? WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_state SET lastSyncTimestamp = ?, serverSeq = ?, syncStatus = 'SYNCED', updatedAt = ? WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkFailed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_state SET syncStatus = 'FAILED', failureCount = failureCount + 1, lastError = ?, updatedAt = ? WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetFailureCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sync_state SET failureCount = 0, lastError = NULL WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByConversationId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(syncState);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<SyncStateEntity> syncStates,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(syncStates);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSyncStateEntity.handle(syncState);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSyncStatus(final String conversationId, final SyncStatus status,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSyncStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromSyncStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, conversationId);
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
          __preparedStmtOfUpdateSyncStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markSynced(final String conversationId, final long timestamp, final Long serverSeq,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        if (serverSeq == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, serverSeq);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 4;
        _stmt.bindString(_argIndex, conversationId);
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
          __preparedStmtOfMarkSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markFailed(final String conversationId, final String error, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkFailed.acquire();
        int _argIndex = 1;
        if (error == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, error);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, conversationId);
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
          __preparedStmtOfMarkFailed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetFailureCount(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetFailureCount.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, conversationId);
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
          __preparedStmtOfResetFailureCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByConversationId(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByConversationId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, conversationId);
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
          __preparedStmtOfDeleteByConversationId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByConversationId(final String conversationId,
      final Continuation<? super SyncStateEntity> $completion) {
    final String _sql = "SELECT * FROM sync_state WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncTimestamp");
          final int _cursorIndexOfServerSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "serverSeq");
          final int _cursorIndexOfLocalSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "localSeq");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastSyncedMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedMessageId");
          final int _cursorIndexOfFailureCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failureCount");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final long _tmpLastSyncTimestamp;
            _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            final Long _tmpServerSeq;
            if (_cursor.isNull(_cursorIndexOfServerSeq)) {
              _tmpServerSeq = null;
            } else {
              _tmpServerSeq = _cursor.getLong(_cursorIndexOfServerSeq);
            }
            final Long _tmpLocalSeq;
            if (_cursor.isNull(_cursorIndexOfLocalSeq)) {
              _tmpLocalSeq = null;
            } else {
              _tmpLocalSeq = _cursor.getLong(_cursorIndexOfLocalSeq);
            }
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __converters.toSyncStatus(_tmp);
            final String _tmpLastSyncedMessageId;
            if (_cursor.isNull(_cursorIndexOfLastSyncedMessageId)) {
              _tmpLastSyncedMessageId = null;
            } else {
              _tmpLastSyncedMessageId = _cursor.getString(_cursorIndexOfLastSyncedMessageId);
            }
            final int _tmpFailureCount;
            _tmpFailureCount = _cursor.getInt(_cursorIndexOfFailureCount);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SyncStateEntity(_tmpConversationId,_tmpLastSyncTimestamp,_tmpServerSeq,_tmpLocalSeq,_tmpSyncStatus,_tmpLastSyncedMessageId,_tmpFailureCount,_tmpLastError,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Flow<SyncStateEntity> getByConversationIdFlow(final String conversationId) {
    final String _sql = "SELECT * FROM sync_state WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncTimestamp");
          final int _cursorIndexOfServerSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "serverSeq");
          final int _cursorIndexOfLocalSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "localSeq");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastSyncedMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedMessageId");
          final int _cursorIndexOfFailureCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failureCount");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final long _tmpLastSyncTimestamp;
            _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            final Long _tmpServerSeq;
            if (_cursor.isNull(_cursorIndexOfServerSeq)) {
              _tmpServerSeq = null;
            } else {
              _tmpServerSeq = _cursor.getLong(_cursorIndexOfServerSeq);
            }
            final Long _tmpLocalSeq;
            if (_cursor.isNull(_cursorIndexOfLocalSeq)) {
              _tmpLocalSeq = null;
            } else {
              _tmpLocalSeq = _cursor.getLong(_cursorIndexOfLocalSeq);
            }
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __converters.toSyncStatus(_tmp);
            final String _tmpLastSyncedMessageId;
            if (_cursor.isNull(_cursorIndexOfLastSyncedMessageId)) {
              _tmpLastSyncedMessageId = null;
            } else {
              _tmpLastSyncedMessageId = _cursor.getString(_cursorIndexOfLastSyncedMessageId);
            }
            final int _tmpFailureCount;
            _tmpFailureCount = _cursor.getInt(_cursorIndexOfFailureCount);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SyncStateEntity(_tmpConversationId,_tmpLastSyncTimestamp,_tmpServerSeq,_tmpLocalSeq,_tmpSyncStatus,_tmpLastSyncedMessageId,_tmpFailureCount,_tmpLastError,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Object getGlobalSyncState(final Continuation<? super SyncStateEntity> $completion) {
    final String _sql = "SELECT * FROM sync_state WHERE conversationId = 'global'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncTimestamp");
          final int _cursorIndexOfServerSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "serverSeq");
          final int _cursorIndexOfLocalSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "localSeq");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastSyncedMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedMessageId");
          final int _cursorIndexOfFailureCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failureCount");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final long _tmpLastSyncTimestamp;
            _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            final Long _tmpServerSeq;
            if (_cursor.isNull(_cursorIndexOfServerSeq)) {
              _tmpServerSeq = null;
            } else {
              _tmpServerSeq = _cursor.getLong(_cursorIndexOfServerSeq);
            }
            final Long _tmpLocalSeq;
            if (_cursor.isNull(_cursorIndexOfLocalSeq)) {
              _tmpLocalSeq = null;
            } else {
              _tmpLocalSeq = _cursor.getLong(_cursorIndexOfLocalSeq);
            }
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __converters.toSyncStatus(_tmp);
            final String _tmpLastSyncedMessageId;
            if (_cursor.isNull(_cursorIndexOfLastSyncedMessageId)) {
              _tmpLastSyncedMessageId = null;
            } else {
              _tmpLastSyncedMessageId = _cursor.getString(_cursorIndexOfLastSyncedMessageId);
            }
            final int _tmpFailureCount;
            _tmpFailureCount = _cursor.getInt(_cursorIndexOfFailureCount);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SyncStateEntity(_tmpConversationId,_tmpLastSyncTimestamp,_tmpServerSeq,_tmpLocalSeq,_tmpSyncStatus,_tmpLastSyncedMessageId,_tmpFailureCount,_tmpLastError,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Flow<List<SyncStateEntity>> getBySyncStatus(final SyncStatus status) {
    final String _sql = "SELECT * FROM sync_state WHERE syncStatus = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromSyncStatus(status);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<List<SyncStateEntity>>() {
      @Override
      @NonNull
      public List<SyncStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncTimestamp");
          final int _cursorIndexOfServerSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "serverSeq");
          final int _cursorIndexOfLocalSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "localSeq");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastSyncedMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedMessageId");
          final int _cursorIndexOfFailureCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failureCount");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<SyncStateEntity> _result = new ArrayList<SyncStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncStateEntity _item;
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final long _tmpLastSyncTimestamp;
            _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            final Long _tmpServerSeq;
            if (_cursor.isNull(_cursorIndexOfServerSeq)) {
              _tmpServerSeq = null;
            } else {
              _tmpServerSeq = _cursor.getLong(_cursorIndexOfServerSeq);
            }
            final Long _tmpLocalSeq;
            if (_cursor.isNull(_cursorIndexOfLocalSeq)) {
              _tmpLocalSeq = null;
            } else {
              _tmpLocalSeq = _cursor.getLong(_cursorIndexOfLocalSeq);
            }
            final SyncStatus _tmpSyncStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __converters.toSyncStatus(_tmp_1);
            final String _tmpLastSyncedMessageId;
            if (_cursor.isNull(_cursorIndexOfLastSyncedMessageId)) {
              _tmpLastSyncedMessageId = null;
            } else {
              _tmpLastSyncedMessageId = _cursor.getString(_cursorIndexOfLastSyncedMessageId);
            }
            final int _tmpFailureCount;
            _tmpFailureCount = _cursor.getInt(_cursorIndexOfFailureCount);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new SyncStateEntity(_tmpConversationId,_tmpLastSyncTimestamp,_tmpServerSeq,_tmpLocalSeq,_tmpSyncStatus,_tmpLastSyncedMessageId,_tmpFailureCount,_tmpLastError,_tmpUpdatedAt);
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
  public Object getPendingSyncStates(
      final Continuation<? super List<SyncStateEntity>> $completion) {
    final String _sql = "SELECT * FROM sync_state WHERE syncStatus = 'PENDING' OR syncStatus = 'FAILED' ORDER BY updatedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SyncStateEntity>>() {
      @Override
      @NonNull
      public List<SyncStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncTimestamp");
          final int _cursorIndexOfServerSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "serverSeq");
          final int _cursorIndexOfLocalSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "localSeq");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfLastSyncedMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedMessageId");
          final int _cursorIndexOfFailureCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failureCount");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<SyncStateEntity> _result = new ArrayList<SyncStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncStateEntity _item;
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final long _tmpLastSyncTimestamp;
            _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            final Long _tmpServerSeq;
            if (_cursor.isNull(_cursorIndexOfServerSeq)) {
              _tmpServerSeq = null;
            } else {
              _tmpServerSeq = _cursor.getLong(_cursorIndexOfServerSeq);
            }
            final Long _tmpLocalSeq;
            if (_cursor.isNull(_cursorIndexOfLocalSeq)) {
              _tmpLocalSeq = null;
            } else {
              _tmpLocalSeq = _cursor.getLong(_cursorIndexOfLocalSeq);
            }
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __converters.toSyncStatus(_tmp);
            final String _tmpLastSyncedMessageId;
            if (_cursor.isNull(_cursorIndexOfLastSyncedMessageId)) {
              _tmpLastSyncedMessageId = null;
            } else {
              _tmpLastSyncedMessageId = _cursor.getString(_cursorIndexOfLastSyncedMessageId);
            }
            final int _tmpFailureCount;
            _tmpFailureCount = _cursor.getInt(_cursorIndexOfFailureCount);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new SyncStateEntity(_tmpConversationId,_tmpLastSyncTimestamp,_tmpServerSeq,_tmpLocalSeq,_tmpSyncStatus,_tmpLastSyncedMessageId,_tmpFailureCount,_tmpLastError,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
