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
import com.aurora.wave.data.entity.ConversationEntity;
import com.aurora.wave.data.model.ConversationType;
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
public final class ConversationDao_Impl implements ConversationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConversationEntity> __insertionAdapterOfConversationEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ConversationEntity> __updateAdapterOfConversationEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastMessage;

  private final SharedSQLiteStatement __preparedStmtOfIncrementUnreadCount;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUnreadCount;

  private final SharedSQLiteStatement __preparedStmtOfClearUnreadCount;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePinned;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMuted;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDraft;

  private final SharedSQLiteStatement __preparedStmtOfUpdateArchived;

  private final SharedSQLiteStatement __preparedStmtOfDeleteConversationById;

  public ConversationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConversationEntity = new EntityInsertionAdapter<ConversationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `conversations` (`id`,`type`,`name`,`avatarUrl`,`lastMessage`,`lastMessageTimestamp`,`unreadCount`,`isPinned`,`isMuted`,`draft`,`isArchived`,`participantIds`,`memberCount`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConversationEntity entity) {
        statement.bindString(1, entity.getId());
        final String _tmp = __converters.fromConversationType(entity.getType());
        statement.bindString(2, _tmp);
        statement.bindString(3, entity.getName());
        if (entity.getAvatarUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAvatarUrl());
        }
        if (entity.getLastMessage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLastMessage());
        }
        statement.bindLong(6, entity.getLastMessageTimestamp());
        statement.bindLong(7, entity.getUnreadCount());
        final int _tmp_1 = entity.isPinned() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isMuted() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        if (entity.getDraft() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDraft());
        }
        final int _tmp_3 = entity.isArchived() ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final String _tmp_4 = __converters.fromStringList(entity.getParticipantIds());
        statement.bindString(12, _tmp_4);
        if (entity.getMemberCount() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getMemberCount());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfConversationEntity = new EntityDeletionOrUpdateAdapter<ConversationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `conversations` SET `id` = ?,`type` = ?,`name` = ?,`avatarUrl` = ?,`lastMessage` = ?,`lastMessageTimestamp` = ?,`unreadCount` = ?,`isPinned` = ?,`isMuted` = ?,`draft` = ?,`isArchived` = ?,`participantIds` = ?,`memberCount` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConversationEntity entity) {
        statement.bindString(1, entity.getId());
        final String _tmp = __converters.fromConversationType(entity.getType());
        statement.bindString(2, _tmp);
        statement.bindString(3, entity.getName());
        if (entity.getAvatarUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAvatarUrl());
        }
        if (entity.getLastMessage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLastMessage());
        }
        statement.bindLong(6, entity.getLastMessageTimestamp());
        statement.bindLong(7, entity.getUnreadCount());
        final int _tmp_1 = entity.isPinned() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isMuted() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        if (entity.getDraft() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDraft());
        }
        final int _tmp_3 = entity.isArchived() ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final String _tmp_4 = __converters.fromStringList(entity.getParticipantIds());
        statement.bindString(12, _tmp_4);
        if (entity.getMemberCount() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getMemberCount());
        }
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLastMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET lastMessage = ?, lastMessageTimestamp = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementUnreadCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET unreadCount = unreadCount + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUnreadCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET unreadCount = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearUnreadCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET unreadCount = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePinned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET isPinned = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMuted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET isMuted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDraft = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET draft = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateArchived = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE conversations SET isArchived = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteConversationById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM conversations WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertConversation(final ConversationEntity conversation,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfConversationEntity.insert(conversation);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertConversations(final List<ConversationEntity> conversations,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfConversationEntity.insert(conversations);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateConversation(final ConversationEntity conversation,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfConversationEntity.handle(conversation);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastMessage(final String conversationId, final String preview,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastMessage.acquire();
        int _argIndex = 1;
        if (preview == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, preview);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
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
          __preparedStmtOfUpdateLastMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementUnreadCount(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementUnreadCount.acquire();
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
          __preparedStmtOfIncrementUnreadCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUnreadCount(final String conversationId, final int count,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUnreadCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
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
          __preparedStmtOfUpdateUnreadCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearUnreadCount(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearUnreadCount.acquire();
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
          __preparedStmtOfClearUnreadCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePinned(final String conversationId, final boolean isPinned,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePinned.acquire();
        int _argIndex = 1;
        final int _tmp = isPinned ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdatePinned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setPinned(final String conversationId, final boolean isPinned,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePinned.acquire();
        int _argIndex = 1;
        final int _tmp = isPinned ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdatePinned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMuted(final String conversationId, final boolean isMuted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMuted.acquire();
        int _argIndex = 1;
        final int _tmp = isMuted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateMuted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setMuted(final String conversationId, final boolean isMuted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMuted.acquire();
        int _argIndex = 1;
        final int _tmp = isMuted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateMuted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDraft(final String conversationId, final String draft,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDraft.acquire();
        int _argIndex = 1;
        if (draft == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, draft);
        }
        _argIndex = 2;
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
          __preparedStmtOfUpdateDraft.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object saveDraft(final String conversationId, final String draft,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDraft.acquire();
        int _argIndex = 1;
        if (draft == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, draft);
        }
        _argIndex = 2;
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
          __preparedStmtOfUpdateDraft.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateArchived(final String conversationId, final boolean archived,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateArchived.acquire();
        int _argIndex = 1;
        final int _tmp = archived ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateArchived.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteConversationById(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteConversationById.acquire();
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
          __preparedStmtOfDeleteConversationById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteConversation(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteConversationById.acquire();
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
          __preparedStmtOfDeleteConversationById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ConversationEntity>> getAllConversations() {
    final String _sql = "SELECT * FROM conversations WHERE isArchived = 0 ORDER BY isPinned DESC, lastMessageTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversations"}, new Callable<List<ConversationEntity>>() {
      @Override
      @NonNull
      public List<ConversationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ConversationEntity> _result = new ArrayList<ConversationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConversationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ConversationEntity>> getAllConversationsFlow() {
    final String _sql = "SELECT * FROM conversations ORDER BY isPinned DESC, lastMessageTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversations"}, new Callable<List<ConversationEntity>>() {
      @Override
      @NonNull
      public List<ConversationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ConversationEntity> _result = new ArrayList<ConversationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConversationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ConversationEntity>> searchConversations(final String query) {
    final String _sql = "SELECT * FROM conversations WHERE name LIKE ? ORDER BY isPinned DESC, lastMessageTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversations"}, new Callable<List<ConversationEntity>>() {
      @Override
      @NonNull
      public List<ConversationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ConversationEntity> _result = new ArrayList<ConversationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConversationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getConversationById(final String conversationId,
      final Continuation<? super ConversationEntity> $completion) {
    final String _sql = "SELECT * FROM conversations WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ConversationEntity>() {
      @Override
      @Nullable
      public ConversationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ConversationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getPrivateConversationWithUser(final String userId,
      final Continuation<? super ConversationEntity> $completion) {
    final String _sql = "SELECT * FROM conversations WHERE type = 'DIRECT' AND participantIds LIKE '%' || ? || '%' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ConversationEntity>() {
      @Override
      @Nullable
      public ConversationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ConversationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<ConversationEntity> getConversationFlow(final String conversationId) {
    final String _sql = "SELECT * FROM conversations WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversations"}, new Callable<ConversationEntity>() {
      @Override
      @Nullable
      public ConversationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final int _cursorIndexOfLastMessageTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTimestamp");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
          final int _cursorIndexOfIsMuted = CursorUtil.getColumnIndexOrThrow(_cursor, "isMuted");
          final int _cursorIndexOfDraft = CursorUtil.getColumnIndexOrThrow(_cursor, "draft");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfParticipantIds = CursorUtil.getColumnIndexOrThrow(_cursor, "participantIds");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ConversationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final ConversationType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConversationType(_tmp);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            final long _tmpLastMessageTimestamp;
            _tmpLastMessageTimestamp = _cursor.getLong(_cursorIndexOfLastMessageTimestamp);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final boolean _tmpIsPinned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp_1 != 0;
            final boolean _tmpIsMuted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsMuted);
            _tmpIsMuted = _tmp_2 != 0;
            final String _tmpDraft;
            if (_cursor.isNull(_cursorIndexOfDraft)) {
              _tmpDraft = null;
            } else {
              _tmpDraft = _cursor.getString(_cursorIndexOfDraft);
            }
            final boolean _tmpIsArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_3 != 0;
            final List<String> _tmpParticipantIds;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfParticipantIds);
            _tmpParticipantIds = __converters.toStringList(_tmp_4);
            final Integer _tmpMemberCount;
            if (_cursor.isNull(_cursorIndexOfMemberCount)) {
              _tmpMemberCount = null;
            } else {
              _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ConversationEntity(_tmpId,_tmpType,_tmpName,_tmpAvatarUrl,_tmpLastMessage,_tmpLastMessageTimestamp,_tmpUnreadCount,_tmpIsPinned,_tmpIsMuted,_tmpDraft,_tmpIsArchived,_tmpParticipantIds,_tmpMemberCount,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<Integer> getTotalUnreadCount() {
    final String _sql = "SELECT SUM(unreadCount) FROM conversations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"conversations"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
