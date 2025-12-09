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
import com.aurora.wave.data.entity.UserEntity;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfSetContactStatus;

  private final SharedSQLiteStatement __preparedStmtOfSetBlockedStatus;

  private final SharedSQLiteStatement __preparedStmtOfSetRemark;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOnlineStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`displayName`,`avatarUrl`,`username`,`statusMessage`,`phoneNumber`,`isContact`,`isBlocked`,`remark`,`tags`,`lastSeenAt`,`isOnline`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getDisplayName());
        if (entity.getAvatarUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAvatarUrl());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUsername());
        }
        if (entity.getStatusMessage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatusMessage());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoneNumber());
        }
        final int _tmp = entity.isContact() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isBlocked() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRemark() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getRemark());
        }
        if (entity.getTags() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTags());
        }
        if (entity.getLastSeenAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastSeenAt());
        }
        final int _tmp_2 = entity.isOnline() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`displayName` = ?,`avatarUrl` = ?,`username` = ?,`statusMessage` = ?,`phoneNumber` = ?,`isContact` = ?,`isBlocked` = ?,`remark` = ?,`tags` = ?,`lastSeenAt` = ?,`isOnline` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getDisplayName());
        if (entity.getAvatarUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAvatarUrl());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUsername());
        }
        if (entity.getStatusMessage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatusMessage());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhoneNumber());
        }
        final int _tmp = entity.isContact() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isBlocked() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRemark() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getRemark());
        }
        if (entity.getTags() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTags());
        }
        if (entity.getLastSeenAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastSeenAt());
        }
        final int _tmp_2 = entity.isOnline() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        statement.bindString(13, entity.getId());
      }
    };
    this.__preparedStmtOfSetContactStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET isContact = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetBlockedStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET isBlocked = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetRemark = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET remark = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOnlineStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET isOnline = ?, lastSeenAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM users WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertUsers(final List<UserEntity> users,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(users);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setContactStatus(final String userId, final boolean isContact,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetContactStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isContact ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfSetContactStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setBlockedStatus(final String userId, final boolean isBlocked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetBlockedStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isBlocked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfSetBlockedStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setRemark(final String userId, final String remark,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetRemark.acquire();
        int _argIndex = 1;
        if (remark == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, remark);
        }
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
          __preparedStmtOfSetRemark.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOnlineStatus(final String userId, final boolean isOnline,
      final Long lastSeenAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOnlineStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isOnline ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (lastSeenAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, lastSeenAt);
        }
        _argIndex = 3;
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
          __preparedStmtOfUpdateOnlineStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUser(final String userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUser.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteUser.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UserEntity>> getContactsFlow() {
    final String _sql = "SELECT * FROM users WHERE isContact = 1 ORDER BY displayName COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfStatusMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "statusMessage");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfIsContact = CursorUtil.getColumnIndexOrThrow(_cursor, "isContact");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final int _cursorIndexOfIsOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "isOnline");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpStatusMessage;
            if (_cursor.isNull(_cursorIndexOfStatusMessage)) {
              _tmpStatusMessage = null;
            } else {
              _tmpStatusMessage = _cursor.getString(_cursorIndexOfStatusMessage);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final boolean _tmpIsContact;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsContact);
            _tmpIsContact = _tmp != 0;
            final boolean _tmpIsBlocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp_1 != 0;
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final Long _tmpLastSeenAt;
            if (_cursor.isNull(_cursorIndexOfLastSeenAt)) {
              _tmpLastSeenAt = null;
            } else {
              _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            }
            final boolean _tmpIsOnline;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOnline);
            _tmpIsOnline = _tmp_2 != 0;
            _item = new UserEntity(_tmpId,_tmpDisplayName,_tmpAvatarUrl,_tmpUsername,_tmpStatusMessage,_tmpPhoneNumber,_tmpIsContact,_tmpIsBlocked,_tmpRemark,_tmpTags,_tmpLastSeenAt,_tmpIsOnline);
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
  public Flow<List<UserEntity>> searchContacts(final String query) {
    final String _sql = "SELECT * FROM users WHERE isContact = 1 AND (displayName LIKE '%' || ? || '%' OR username LIKE '%' || ? || '%' OR remark LIKE '%' || ? || '%') ORDER BY displayName COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<List<UserEntity>>() {
      @Override
      @NonNull
      public List<UserEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfStatusMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "statusMessage");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfIsContact = CursorUtil.getColumnIndexOrThrow(_cursor, "isContact");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final int _cursorIndexOfIsOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "isOnline");
          final List<UserEntity> _result = new ArrayList<UserEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpStatusMessage;
            if (_cursor.isNull(_cursorIndexOfStatusMessage)) {
              _tmpStatusMessage = null;
            } else {
              _tmpStatusMessage = _cursor.getString(_cursorIndexOfStatusMessage);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final boolean _tmpIsContact;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsContact);
            _tmpIsContact = _tmp != 0;
            final boolean _tmpIsBlocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp_1 != 0;
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final Long _tmpLastSeenAt;
            if (_cursor.isNull(_cursorIndexOfLastSeenAt)) {
              _tmpLastSeenAt = null;
            } else {
              _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            }
            final boolean _tmpIsOnline;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOnline);
            _tmpIsOnline = _tmp_2 != 0;
            _item = new UserEntity(_tmpId,_tmpDisplayName,_tmpAvatarUrl,_tmpUsername,_tmpStatusMessage,_tmpPhoneNumber,_tmpIsContact,_tmpIsBlocked,_tmpRemark,_tmpTags,_tmpLastSeenAt,_tmpIsOnline);
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
  public Object getUserById(final String userId,
      final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfStatusMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "statusMessage");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfIsContact = CursorUtil.getColumnIndexOrThrow(_cursor, "isContact");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final int _cursorIndexOfIsOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "isOnline");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpStatusMessage;
            if (_cursor.isNull(_cursorIndexOfStatusMessage)) {
              _tmpStatusMessage = null;
            } else {
              _tmpStatusMessage = _cursor.getString(_cursorIndexOfStatusMessage);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final boolean _tmpIsContact;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsContact);
            _tmpIsContact = _tmp != 0;
            final boolean _tmpIsBlocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp_1 != 0;
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final Long _tmpLastSeenAt;
            if (_cursor.isNull(_cursorIndexOfLastSeenAt)) {
              _tmpLastSeenAt = null;
            } else {
              _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            }
            final boolean _tmpIsOnline;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOnline);
            _tmpIsOnline = _tmp_2 != 0;
            _result = new UserEntity(_tmpId,_tmpDisplayName,_tmpAvatarUrl,_tmpUsername,_tmpStatusMessage,_tmpPhoneNumber,_tmpIsContact,_tmpIsBlocked,_tmpRemark,_tmpTags,_tmpLastSeenAt,_tmpIsOnline);
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
  public Flow<UserEntity> getUserFlow(final String userId) {
    final String _sql = "SELECT * FROM users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfStatusMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "statusMessage");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfIsContact = CursorUtil.getColumnIndexOrThrow(_cursor, "isContact");
          final int _cursorIndexOfIsBlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlocked");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final int _cursorIndexOfIsOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "isOnline");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpAvatarUrl;
            if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
              _tmpAvatarUrl = null;
            } else {
              _tmpAvatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpStatusMessage;
            if (_cursor.isNull(_cursorIndexOfStatusMessage)) {
              _tmpStatusMessage = null;
            } else {
              _tmpStatusMessage = _cursor.getString(_cursorIndexOfStatusMessage);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final boolean _tmpIsContact;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsContact);
            _tmpIsContact = _tmp != 0;
            final boolean _tmpIsBlocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlocked);
            _tmpIsBlocked = _tmp_1 != 0;
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            final String _tmpTags;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmpTags = null;
            } else {
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
            }
            final Long _tmpLastSeenAt;
            if (_cursor.isNull(_cursorIndexOfLastSeenAt)) {
              _tmpLastSeenAt = null;
            } else {
              _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            }
            final boolean _tmpIsOnline;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsOnline);
            _tmpIsOnline = _tmp_2 != 0;
            _result = new UserEntity(_tmpId,_tmpDisplayName,_tmpAvatarUrl,_tmpUsername,_tmpStatusMessage,_tmpPhoneNumber,_tmpIsContact,_tmpIsBlocked,_tmpRemark,_tmpTags,_tmpLastSeenAt,_tmpIsOnline);
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
  public Flow<Integer> getContactCount() {
    final String _sql = "SELECT COUNT(*) FROM users WHERE isContact = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<Integer>() {
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
