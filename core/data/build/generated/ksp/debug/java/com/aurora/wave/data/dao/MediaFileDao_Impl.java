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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aurora.wave.data.converter.Converters;
import com.aurora.wave.data.entity.MediaFileEntity;
import com.aurora.wave.data.model.DownloadStatus;
import com.aurora.wave.data.model.MediaType;
import com.aurora.wave.data.model.UploadStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class MediaFileDao_Impl implements MediaFileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MediaFileEntity> __insertionAdapterOfMediaFileEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<MediaFileEntity> __updateAdapterOfMediaFileEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDownloadStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUploadStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLocalPath;

  private final SharedSQLiteStatement __preparedStmtOfUpdateThumbnailPath;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastAccessedAt;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByMessageId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByConversation;

  public MediaFileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMediaFileEntity = new EntityInsertionAdapter<MediaFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `media_files` (`id`,`messageId`,`conversationId`,`type`,`fileName`,`fileSize`,`mimeType`,`localPath`,`remotePath`,`thumbnailPath`,`thumbnailRemotePath`,`downloadStatus`,`uploadStatus`,`progress`,`width`,`height`,`duration`,`md5Hash`,`createdAt`,`lastAccessedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaFileEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMessageId());
        statement.bindString(3, entity.getConversationId());
        final String _tmp = __converters.fromMediaType(entity.getType());
        statement.bindString(4, _tmp);
        statement.bindString(5, entity.getFileName());
        statement.bindLong(6, entity.getFileSize());
        statement.bindString(7, entity.getMimeType());
        if (entity.getLocalPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocalPath());
        }
        statement.bindString(9, entity.getRemotePath());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getThumbnailPath());
        }
        if (entity.getThumbnailRemotePath() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getThumbnailRemotePath());
        }
        final String _tmp_1 = __converters.fromDownloadStatus(entity.getDownloadStatus());
        statement.bindString(12, _tmp_1);
        final String _tmp_2 = __converters.fromUploadStatus(entity.getUploadStatus());
        statement.bindString(13, _tmp_2);
        statement.bindLong(14, entity.getProgress());
        if (entity.getWidth() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getWidth());
        }
        if (entity.getHeight() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getHeight());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getDuration());
        }
        if (entity.getMd5Hash() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getMd5Hash());
        }
        statement.bindLong(19, entity.getCreatedAt());
        statement.bindLong(20, entity.getLastAccessedAt());
      }
    };
    this.__updateAdapterOfMediaFileEntity = new EntityDeletionOrUpdateAdapter<MediaFileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `media_files` SET `id` = ?,`messageId` = ?,`conversationId` = ?,`type` = ?,`fileName` = ?,`fileSize` = ?,`mimeType` = ?,`localPath` = ?,`remotePath` = ?,`thumbnailPath` = ?,`thumbnailRemotePath` = ?,`downloadStatus` = ?,`uploadStatus` = ?,`progress` = ?,`width` = ?,`height` = ?,`duration` = ?,`md5Hash` = ?,`createdAt` = ?,`lastAccessedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MediaFileEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getMessageId());
        statement.bindString(3, entity.getConversationId());
        final String _tmp = __converters.fromMediaType(entity.getType());
        statement.bindString(4, _tmp);
        statement.bindString(5, entity.getFileName());
        statement.bindLong(6, entity.getFileSize());
        statement.bindString(7, entity.getMimeType());
        if (entity.getLocalPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocalPath());
        }
        statement.bindString(9, entity.getRemotePath());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getThumbnailPath());
        }
        if (entity.getThumbnailRemotePath() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getThumbnailRemotePath());
        }
        final String _tmp_1 = __converters.fromDownloadStatus(entity.getDownloadStatus());
        statement.bindString(12, _tmp_1);
        final String _tmp_2 = __converters.fromUploadStatus(entity.getUploadStatus());
        statement.bindString(13, _tmp_2);
        statement.bindLong(14, entity.getProgress());
        if (entity.getWidth() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getWidth());
        }
        if (entity.getHeight() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getHeight());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getDuration());
        }
        if (entity.getMd5Hash() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getMd5Hash());
        }
        statement.bindLong(19, entity.getCreatedAt());
        statement.bindLong(20, entity.getLastAccessedAt());
        statement.bindString(21, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDownloadStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_files SET downloadStatus = ?, progress = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUploadStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_files SET uploadStatus = ?, progress = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLocalPath = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_files SET localPath = ?, downloadStatus = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateThumbnailPath = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_files SET thumbnailPath = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastAccessedAt = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE media_files SET lastAccessedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_files WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByMessageId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_files WHERE messageId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByConversation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM media_files WHERE conversationId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MediaFileEntity mediaFile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMediaFileEntity.insert(mediaFile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MediaFileEntity> mediaFiles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMediaFileEntity.insert(mediaFiles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MediaFileEntity mediaFile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMediaFileEntity.handle(mediaFile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDownloadStatus(final String id, final DownloadStatus status,
      final int progress, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDownloadStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromDownloadStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, progress);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateDownloadStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUploadStatus(final String id, final UploadStatus status, final int progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUploadStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromUploadStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, progress);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateUploadStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLocalPath(final String id, final String localPath,
      final DownloadStatus status, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLocalPath.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, localPath);
        _argIndex = 2;
        final String _tmp = __converters.fromDownloadStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateLocalPath.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateThumbnailPath(final String id, final String thumbnailPath,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateThumbnailPath.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, thumbnailPath);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateThumbnailPath.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastAccessedAt(final String id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastAccessedAt.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateLastAccessedAt.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
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
  public Object getById(final String id, final Continuation<? super MediaFileEntity> $completion) {
    final String _sql = "SELECT * FROM media_files WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaFileEntity>() {
      @Override
      @Nullable
      public MediaFileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final MediaFileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_1);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_2);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _result = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Object getByMessageId(final String messageId,
      final Continuation<? super MediaFileEntity> $completion) {
    final String _sql = "SELECT * FROM media_files WHERE messageId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MediaFileEntity>() {
      @Override
      @Nullable
      public MediaFileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final MediaFileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_1);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_2);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _result = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<MediaFileEntity> getByMessageIdFlow(final String messageId) {
    final String _sql = "SELECT * FROM media_files WHERE messageId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<MediaFileEntity>() {
      @Override
      @Nullable
      public MediaFileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final MediaFileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_1);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_2);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _result = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<List<MediaFileEntity>> getByConversation(final String conversationId) {
    final String _sql = "SELECT * FROM media_files WHERE conversationId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_1);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_2);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<List<MediaFileEntity>> getByConversationAndType(final String conversationId,
      final MediaType type) {
    final String _sql = "SELECT * FROM media_files WHERE conversationId = ? AND type = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    _argIndex = 2;
    final String _tmp = __converters.fromMediaType(type);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp_1);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_2);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_3);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<List<MediaFileEntity>> getByType(final MediaType type, final int limit) {
    final String _sql = "SELECT * FROM media_files WHERE type = ? ORDER BY createdAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromMediaType(type);
    _statement.bindString(_argIndex, _tmp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp_1);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_2);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_3);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<List<MediaFileEntity>> getByDownloadStatus(final DownloadStatus status) {
    final String _sql = "SELECT * FROM media_files WHERE downloadStatus = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromDownloadStatus(status);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp_1);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_2);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_3);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Flow<List<MediaFileEntity>> getByUploadStatus(final UploadStatus status) {
    final String _sql = "SELECT * FROM media_files WHERE uploadStatus = ? ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromUploadStatus(status);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"media_files"}, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp_1);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_2);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_3);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Object getTotalSizeByConversation(final String conversationId,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(fileSize) FROM media_files WHERE conversationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, conversationId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Object getTotalSize(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(fileSize) FROM media_files";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Object getCountByDownloadStatus(final DownloadStatus status,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM media_files WHERE downloadStatus = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromDownloadStatus(status);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
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
  public Object getOldCachedFiles(final long beforeTimestamp, final int limit,
      final Continuation<? super List<MediaFileEntity>> $completion) {
    final String _sql = "SELECT * FROM media_files WHERE lastAccessedAt < ? AND localPath IS NOT NULL ORDER BY lastAccessedAt ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, beforeTimestamp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MediaFileEntity>>() {
      @Override
      @NonNull
      public List<MediaFileEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfConversationId = CursorUtil.getColumnIndexOrThrow(_cursor, "conversationId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remotePath");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final int _cursorIndexOfThumbnailRemotePath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailRemotePath");
          final int _cursorIndexOfDownloadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadStatus");
          final int _cursorIndexOfUploadStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadStatus");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfMd5Hash = CursorUtil.getColumnIndexOrThrow(_cursor, "md5Hash");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfLastAccessedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessedAt");
          final List<MediaFileEntity> _result = new ArrayList<MediaFileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MediaFileEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpMessageId;
            _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            final String _tmpConversationId;
            _tmpConversationId = _cursor.getString(_cursorIndexOfConversationId);
            final MediaType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toMediaType(_tmp);
            final String _tmpFileName;
            _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpRemotePath;
            _tmpRemotePath = _cursor.getString(_cursorIndexOfRemotePath);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            final String _tmpThumbnailRemotePath;
            if (_cursor.isNull(_cursorIndexOfThumbnailRemotePath)) {
              _tmpThumbnailRemotePath = null;
            } else {
              _tmpThumbnailRemotePath = _cursor.getString(_cursorIndexOfThumbnailRemotePath);
            }
            final DownloadStatus _tmpDownloadStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDownloadStatus);
            _tmpDownloadStatus = __converters.toDownloadStatus(_tmp_1);
            final UploadStatus _tmpUploadStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUploadStatus);
            _tmpUploadStatus = __converters.toUploadStatus(_tmp_2);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final Integer _tmpWidth;
            if (_cursor.isNull(_cursorIndexOfWidth)) {
              _tmpWidth = null;
            } else {
              _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            }
            final Integer _tmpHeight;
            if (_cursor.isNull(_cursorIndexOfHeight)) {
              _tmpHeight = null;
            } else {
              _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            }
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            final String _tmpMd5Hash;
            if (_cursor.isNull(_cursorIndexOfMd5Hash)) {
              _tmpMd5Hash = null;
            } else {
              _tmpMd5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpLastAccessedAt;
            _tmpLastAccessedAt = _cursor.getLong(_cursorIndexOfLastAccessedAt);
            _item = new MediaFileEntity(_tmpId,_tmpMessageId,_tmpConversationId,_tmpType,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpLocalPath,_tmpRemotePath,_tmpThumbnailPath,_tmpThumbnailRemotePath,_tmpDownloadStatus,_tmpUploadStatus,_tmpProgress,_tmpWidth,_tmpHeight,_tmpDuration,_tmpMd5Hash,_tmpCreatedAt,_tmpLastAccessedAt);
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
  public Object clearLocalPaths(final List<String> ids,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE media_files SET localPath = NULL, thumbnailPath = NULL, downloadStatus = 'PENDING' WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : ids) {
          _stmt.bindString(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
