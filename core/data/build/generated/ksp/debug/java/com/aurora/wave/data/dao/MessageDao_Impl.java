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
import com.aurora.wave.data.entity.MessageEntity;
import com.aurora.wave.data.model.DeliveryStatus;
import com.aurora.wave.data.model.MessageType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageEntity> __insertionAdapterOfMessageEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<MessageEntity> __updateAdapterOfMessageEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDeliveryStatus;

  private final SharedSQLiteStatement __preparedStmtOfMarkAllAsRead;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessageById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessagesForConversation;

  public MessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageEntity = new EntityInsertionAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `messages` (`id`,`conversationId`,`senderId`,`senderName`,`senderAvatar`,`type`,`content`,`timestamp`,`deliveryStatus`,`replyToMessageId`,`forwardedFromId`,`isOutgoing`,`isEdited`,`editedAt`,`mentions`,`reactions`,`isDeleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getConversationId());
        statement.bindString(3, entity.getSenderId());
        if (entity.getSenderName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSenderName());
        }
        if (entity.getSenderAvatar() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSenderAvatar());
        }
        final String _tmp = __converters.fromMessageType(entity.getType());
        statement.bindString(6, _tmp);
        statement.bindString(7, entity.getContent());
        statement.bindLong(8, entity.getTimestamp());
        final String _tmp_1 = __converters.fromDeliveryStatus(entity.getDeliveryStatus());
        statement.bindString(9, _tmp_1);
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getReplyToMessageId());
        }
        if (entity.getForwardedFromId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getForwardedFromId());
        }
        final int _tmp_2 = entity.isOutgoing() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isEdited() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        if (entity.getEditedAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getEditedAt());
        }
        if (entity.getMentions() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getMentions());
        }
        if (entity.getReactions() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getReactions());
        }
        final int _tmp_4 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(17, _tmp_4);
      }
    };
    this.__updateAdapterOfMessageEntity = new EntityDeletionOrUpdateAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `messages` SET `id` = ?,`conversationId` = ?,`senderId` = ?,`senderName` = ?,`senderAvatar` = ?,`type` = ?,`content` = ?,`timestamp` = ?,`deliveryStatus` = ?,`replyToMessageId` = ?,`forwardedFromId` = ?,`isOutgoing` = ?,`isEdited` = ?,`editedAt` = ?,`mentions` = ?,`reactions` = ?,`isDeleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getConversationId());
        statement.bindString(3, entity.getSenderId());
        if (entity.getSenderName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSenderName());
        }
        if (entity.getSenderAvatar() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSenderAvatar());
        }
        final String _tmp = __converters.fromMessageType(entity.getType());
        statement.bindString(6, _tmp);
        statement.bindString(7, entity.getContent());
        statement.bindLong(8, entity.getTimestamp());
        final String _tmp_1 = __converters.fromDeliveryStatus(entity.getDeliveryStatus());
        statement.bindString(9, _tmp_1);
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getReplyToMessageId());
        }
        if (entity.getForwardedFromId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getForwardedFromId());
        }
        final int _tmp_2 = entity.isOutgoing() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isEdited() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        if (entity.getEditedAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getEditedAt());
        }
        if (entity.getMentions() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getMentions());
        }
        if (entity.getReactions() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getReactions());
        }
        final int _tmp_4 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(17, _tmp_4);
        statement.bindString(18, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDeliveryStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET deliveryStatus = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAllAsRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET deliveryStatus = ? WHERE conversationId = ? AND senderId != ?";
        return _query;
      }
    };
    this.__preparedStmtOfSoftDeleteMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET isDeleted = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessageById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM messages WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessagesForConversation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM messages WHERE conversationId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMessage(final MessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageEntity.insert(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMessages(final List<MessageEntity> messages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageEntity.insert(messages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMessage(final MessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageEntity.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDeliveryStatus(final String messageId, final DeliveryStatus status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDeliveryStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromDeliveryStatus(status);
        _stmt.bindString(_argIndex, _tmp);
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
          __preparedStmtOfUpdateDeliveryStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAllAsRead(final String conversationId, final DeliveryStatus status,
      final String currentUserId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAllAsRead.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromDeliveryStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, conversationId);
        _argIndex = 3;
        _stmt.bindString(_argIndex, currentUserId);
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
          __preparedStmtOfMarkAllAsRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object softDeleteMessage(final String messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteMessage.acquire();
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
          __preparedStmtOfSoftDeleteMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessageById(final String messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessageById.acquire();
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
          __preparedStmtOfDeleteMessageById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessagesForConversation(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessagesForConversation.acquire();
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
          __preparedStmtOfDeleteMessagesForConversation.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllMessagesInConversation(final String conversationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessagesForConversation.acquire();
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
          __preparedStmtOfDeleteMessagesForConversation.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MessageEntity>> getMessagesFlow(final String conversationId) {
    final String _sql = "SELECT * FROM messages WHERE conversationId = ? AND isDeleted = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _item = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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
  public Flow<List<MessageEntity>> getMessagesForConversation(final String conversationId) {
    final String _sql = "SELECT * FROM messages WHERE conversationId = ? AND isDeleted = 0 ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _item = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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
  public Flow<List<MessageEntity>> getRecentMessages(final String conversationId, final int limit) {
    final String _sql = "SELECT * FROM messages WHERE conversationId = ? AND isDeleted = 0 ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _item = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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
  public Object loadMoreMessages(final String conversationId, final long beforeTimestamp,
      final int limit, final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE conversationId = ? AND timestamp < ? AND isDeleted = 0 ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, beforeTimestamp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _item = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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
  public Object getMessageById(final String messageId,
      final Continuation<? super MessageEntity> $completion) {
    final String _sql = "SELECT * FROM messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageEntity>() {
      @Override
      @Nullable
      public MessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final MessageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _result = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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
  public Object getMessageCount(final String conversationId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM messages WHERE conversationId = ? AND isDeleted = 0";
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
  public Object getUnreadCountForConversation(final String conversationId,
      final String currentUserId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM messages WHERE conversationId = ? AND deliveryStatus != 'READ' AND senderId != ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    _statement.bindString(_argIndex, currentUserId);
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
  public Flow<List<MessageEntity>> searchMessages(final String query) {
    final String _sql = "SELECT * FROM messages WHERE content LIKE ? AND isDeleted = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "senderAvatar");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfForwardedFromId = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardedFromId");
          final int _cursorIndexOfIsOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isOutgoing");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfMentions = CursorUtil.getColumnIndexOrThrow(_cursor, "mentions");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            if (_cursor.isNull(_cursorIndexOfSenderName)) {
              _tmpSenderName = null;
            } else {
              _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            }
            final String _tmpSenderAvatar;
            if (_cursor.isNull(_cursorIndexOfSenderAvatar)) {
              _tmpSenderAvatar = null;
            } else {
              _tmpSenderAvatar = _cursor.getString(_cursorIndexOfSenderAvatar);
            }
            final MessageType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final DeliveryStatus _tmpDeliveryStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDeliveryStatus);
            _tmpDeliveryStatus = __converters.toDeliveryStatus(_tmp_1);
            final String _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            }
            final String _tmpForwardedFromId;
            if (_cursor.isNull(_cursorIndexOfForwardedFromId)) {
              _tmpForwardedFromId = null;
            } else {
              _tmpForwardedFromId = _cursor.getString(_cursorIndexOfForwardedFromId);
            }
            final boolean _tmpIsOutgoing;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOutgoing);
            _tmpIsOutgoing = _tmp_2 != 0;
            final boolean _tmpIsEdited;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_3 != 0;
            final Long _tmpEditedAt;
            if (_cursor.isNull(_cursorIndexOfEditedAt)) {
              _tmpEditedAt = null;
            } else {
              _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            }
            final String _tmpMentions;
            if (_cursor.isNull(_cursorIndexOfMentions)) {
              _tmpMentions = null;
            } else {
              _tmpMentions = _cursor.getString(_cursorIndexOfMentions);
            }
            final String _tmpReactions;
            if (_cursor.isNull(_cursorIndexOfReactions)) {
              _tmpReactions = null;
            } else {
              _tmpReactions = _cursor.getString(_cursorIndexOfReactions);
            }
            final boolean _tmpIsDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_4 != 0;
            _item = new MessageEntity(_tmpId,_tmpConversationId,_tmpSenderId,_tmpSenderName,_tmpSenderAvatar,_tmpType,_tmpContent,_tmpTimestamp,_tmpDeliveryStatus,_tmpReplyToMessageId,_tmpForwardedFromId,_tmpIsOutgoing,_tmpIsEdited,_tmpEditedAt,_tmpMentions,_tmpReactions,_tmpIsDeleted);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
