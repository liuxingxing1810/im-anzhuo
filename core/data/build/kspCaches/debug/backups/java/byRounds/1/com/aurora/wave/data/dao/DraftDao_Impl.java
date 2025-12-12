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
import com.aurora.wave.data.entity.DraftEntity;
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
public final class DraftDao_Impl implements DraftDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DraftEntity> __insertionAdapterOfDraftEntity;

  private final EntityDeletionOrUpdateAdapter<DraftEntity> __updateAdapterOfDraftEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateContent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByConversationId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public DraftDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDraftEntity = new EntityInsertionAdapter<DraftEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `drafts` (`conversationId`,`content`,`replyToMessageId`,`mentionUserIds`,`attachmentIds`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DraftEntity entity) {
        statement.bindString(1, entity.getConversationId());
        statement.bindString(2, entity.getContent());
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getReplyToMessageId());
        }
        if (entity.getMentionUserIds() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMentionUserIds());
        }
        if (entity.getAttachmentIds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAttachmentIds());
        }
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfDraftEntity = new EntityDeletionOrUpdateAdapter<DraftEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `drafts` SET `conversationId` = ?,`content` = ?,`replyToMessageId` = ?,`mentionUserIds` = ?,`attachmentIds` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `conversationId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DraftEntity entity) {
        statement.bindString(1, entity.getConversationId());
        statement.bindString(2, entity.getContent());
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getReplyToMessageId());
        }
        if (entity.getMentionUserIds() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMentionUserIds());
        }
        if (entity.getAttachmentIds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAttachmentIds());
        }
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindString(8, entity.getConversationId());
      }
    };
    this.__preparedStmtOfUpdateContent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE drafts SET content = ?, updatedAt = ? WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByConversationId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM drafts WHERE conversationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM drafts";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DraftEntity draft, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDraftEntity.insert(draft);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final DraftEntity draft, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDraftEntity.handle(draft);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateContent(final String conversationId, final String content,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateContent.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, content);
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
          __preparedStmtOfUpdateContent.release(_stmt);
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
      final Continuation<? super DraftEntity> $completion) {
    final String _sql = "SELECT * FROM drafts WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DraftEntity>() {
      @Override
      @Nullable
      public DraftEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfMentionUserIds = CursorUtil.getColumnIndexOrThrow(_cursor, "mentionUserIds");
          final int _cursorIndexOfAttachmentIds = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentIds");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final DraftEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpMentionUserIds;
            if (_cursor.isNull(_cursorIndexOfMentionUserIds)) {
              _tmpMentionUserIds = null;
            } else {
              _tmpMentionUserIds = _cursor.getString(_cursorIndexOfMentionUserIds);
            }
            final String _tmpAttachmentIds;
            if (_cursor.isNull(_cursorIndexOfAttachmentIds)) {
              _tmpAttachmentIds = null;
            } else {
              _tmpAttachmentIds = _cursor.getString(_cursorIndexOfAttachmentIds);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new DraftEntity(_tmpConversationId,_tmpContent,_tmpReplyToMessageId,_tmpMentionUserIds,_tmpAttachmentIds,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<DraftEntity> getByConversationIdFlow(final String conversationId) {
    final String _sql = "SELECT * FROM drafts WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"drafts"}, new Callable<DraftEntity>() {
      @Override
      @Nullable
      public DraftEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfMentionUserIds = CursorUtil.getColumnIndexOrThrow(_cursor, "mentionUserIds");
          final int _cursorIndexOfAttachmentIds = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentIds");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final DraftEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpMentionUserIds;
            if (_cursor.isNull(_cursorIndexOfMentionUserIds)) {
              _tmpMentionUserIds = null;
            } else {
              _tmpMentionUserIds = _cursor.getString(_cursorIndexOfMentionUserIds);
            }
            final String _tmpAttachmentIds;
            if (_cursor.isNull(_cursorIndexOfAttachmentIds)) {
              _tmpAttachmentIds = null;
            } else {
              _tmpAttachmentIds = _cursor.getString(_cursorIndexOfAttachmentIds);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new DraftEntity(_tmpConversationId,_tmpContent,_tmpReplyToMessageId,_tmpMentionUserIds,_tmpAttachmentIds,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<DraftEntity>> getAllDrafts() {
    final String _sql = "SELECT * FROM drafts ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"drafts"}, new Callable<List<DraftEntity>>() {
      @Override
      @NonNull
      public List<DraftEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfMentionUserIds = CursorUtil.getColumnIndexOrThrow(_cursor, "mentionUserIds");
          final int _cursorIndexOfAttachmentIds = CursorUtil.getColumnIndexOrThrow(_cursor, "attachmentIds");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<DraftEntity> _result = new ArrayList<DraftEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DraftEntity _item;
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpMentionUserIds;
            if (_cursor.isNull(_cursorIndexOfMentionUserIds)) {
              _tmpMentionUserIds = null;
            } else {
              _tmpMentionUserIds = _cursor.getString(_cursorIndexOfMentionUserIds);
            }
            final String _tmpAttachmentIds;
            if (_cursor.isNull(_cursorIndexOfAttachmentIds)) {
              _tmpAttachmentIds = null;
            } else {
              _tmpAttachmentIds = _cursor.getString(_cursorIndexOfAttachmentIds);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DraftEntity(_tmpConversationId,_tmpContent,_tmpReplyToMessageId,_tmpMentionUserIds,_tmpAttachmentIds,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getDraftCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM drafts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
