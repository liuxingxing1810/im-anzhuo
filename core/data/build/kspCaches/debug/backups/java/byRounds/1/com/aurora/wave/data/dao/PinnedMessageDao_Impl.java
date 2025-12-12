package com.aurora.wave.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aurora.wave.data.entity.PinnedMessageEntity;
import java.lang.Boolean;
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
public final class PinnedMessageDao_Impl implements PinnedMessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PinnedMessageEntity> __insertionAdapterOfPinnedMessageEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSortOrder;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByConversation;

  public PinnedMessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPinnedMessageEntity = new EntityInsertionAdapter<PinnedMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pinned_messages` (`conversationId`,`messageId`,`pinnedByUserId`,`pinnedByUserName`,`pinnedAt`,`sortOrder`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PinnedMessageEntity entity) {
        statement.bindString(1, entity.getConversationId());
        statement.bindString(2, entity.getMessageId());
        statement.bindString(3, entity.getPinnedByUserId());
        if (entity.getPinnedByUserName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPinnedByUserName());
        }
        statement.bindLong(5, entity.getPinnedAt());
        statement.bindLong(6, entity.getSortOrder());
      }
    };
    this.__preparedStmtOfUpdateSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pinned_messages SET sortOrder = ? WHERE conversationId = ? AND messageId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pinned_messages WHERE conversationId = ? AND messageId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByConversation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pinned_messages WHERE conversationId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PinnedMessageEntity pinnedMessage,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPinnedMessageEntity.insert(pinnedMessage);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<PinnedMessageEntity> pinnedMessages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPinnedMessageEntity.insert(pinnedMessages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSortOrder(final String conversationId, final String messageId,
      final int sortOrder, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSortOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sortOrder);
        _argIndex = 2;
        _stmt.bindString(_argIndex, conversationId);
        _argIndex = 3;
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
          __preparedStmtOfUpdateSortOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String conversationId, final String messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, conversationId);
        _argIndex = 2;
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
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByConversation(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByConversation.acquire();
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
          __preparedStmtOfDeleteByConversation.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PinnedMessageEntity>> getByConversationId(final String conversationId) {
    final String _sql = "SELECT * FROM pinned_messages WHERE conversationId = ? ORDER BY sortOrder ASC, pinnedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pinned_messages"}, new Callable<List<PinnedMessageEntity>>() {
      @Override
      @NonNull
      public List<PinnedMessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfPinnedByUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedByUserId");
          final int _cursorIndexOfPinnedByUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedByUserName");
          final int _cursorIndexOfPinnedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedAt");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<PinnedMessageEntity> _result = new ArrayList<PinnedMessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PinnedMessageEntity _item;
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpPinnedByUserId;
            _tmpPinnedByUserId = _cursor.getString(_cursorIndexOfPinnedByUserId);
            final String _tmpPinnedByUserName;
            if (_cursor.isNull(_cursorIndexOfPinnedByUserName)) {
              _tmpPinnedByUserName = null;
            } else {
              _tmpPinnedByUserName = _cursor.getString(_cursorIndexOfPinnedByUserName);
            }
            final long _tmpPinnedAt;
            _tmpPinnedAt = _cursor.getLong(_cursorIndexOfPinnedAt);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new PinnedMessageEntity(_tmpConversationId,_tmpMessageId,_tmpPinnedByUserId,_tmpPinnedByUserName,_tmpPinnedAt,_tmpSortOrder);
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
  public Object getByConversationAndMessage(final String conversationId, final String messageId,
      final Continuation<? super PinnedMessageEntity> $completion) {
    final String _sql = "SELECT * FROM pinned_messages WHERE conversationId = ? AND messageId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PinnedMessageEntity>() {
      @Override
      @Nullable
      public PinnedMessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfPinnedByUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedByUserId");
          final int _cursorIndexOfPinnedByUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedByUserName");
          final int _cursorIndexOfPinnedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pinnedAt");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final PinnedMessageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpPinnedByUserId;
            _tmpPinnedByUserId = _cursor.getString(_cursorIndexOfPinnedByUserId);
            final String _tmpPinnedByUserName;
            if (_cursor.isNull(_cursorIndexOfPinnedByUserName)) {
              _tmpPinnedByUserName = null;
            } else {
              _tmpPinnedByUserName = _cursor.getString(_cursorIndexOfPinnedByUserName);
            }
            final long _tmpPinnedAt;
            _tmpPinnedAt = _cursor.getLong(_cursorIndexOfPinnedAt);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _result = new PinnedMessageEntity(_tmpConversationId,_tmpMessageId,_tmpPinnedByUserId,_tmpPinnedByUserName,_tmpPinnedAt,_tmpSortOrder);
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
  public Object getPinnedCount(final String conversationId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM pinned_messages WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
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

  @Override
  public Object isPinned(final String conversationId, final String messageId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM pinned_messages WHERE conversationId = ? AND messageId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
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
