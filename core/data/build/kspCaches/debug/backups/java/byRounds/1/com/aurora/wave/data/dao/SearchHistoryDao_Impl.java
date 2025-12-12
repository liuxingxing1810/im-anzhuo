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
import com.aurora.wave.data.entity.SearchHistoryEntity;
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
public final class SearchHistoryDao_Impl implements SearchHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SearchHistoryEntity> __insertionAdapterOfSearchHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSearchTime;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByKeyword;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfKeepRecentOnly;

  public SearchHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSearchHistoryEntity = new EntityInsertionAdapter<SearchHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `search_history` (`id`,`keyword`,`searchType`,`searchedAt`,`resultCount`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SearchHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getKeyword());
        statement.bindString(3, entity.getSearchType());
        statement.bindLong(4, entity.getSearchedAt());
        if (entity.getResultCount() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getResultCount());
        }
      }
    };
    this.__preparedStmtOfUpdateSearchTime = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE search_history SET searchedAt = ?, resultCount = ? WHERE keyword = ? AND searchType = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM search_history WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByKeyword = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM search_history WHERE keyword = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM search_history";
        return _query;
      }
    };
    this.__preparedStmtOfKeepRecentOnly = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY searchedAt DESC LIMIT ?)";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SearchHistoryEntity searchHistory,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSearchHistoryEntity.insert(searchHistory);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSearchTime(final String keyword, final String searchType,
      final long timestamp, final Integer resultCount,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSearchTime.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        if (resultCount == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, resultCount);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, keyword);
        _argIndex = 4;
        _stmt.bindString(_argIndex, searchType);
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
          __preparedStmtOfUpdateSearchTime.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
  public Object deleteByKeyword(final String keyword,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByKeyword.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, keyword);
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
          __preparedStmtOfDeleteByKeyword.release(_stmt);
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
  public Object keepRecentOnly(final int keepCount, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfKeepRecentOnly.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, keepCount);
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
          __preparedStmtOfKeepRecentOnly.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SearchHistoryEntity>> getRecentSearches(final int limit) {
    final String _sql = "SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"search_history"}, new Callable<List<SearchHistoryEntity>>() {
      @Override
      @NonNull
      public List<SearchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "keyword");
          final int _cursorIndexOfSearchType = CursorUtil.getColumnIndexOrThrow(_cursor, "searchType");
          final int _cursorIndexOfSearchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "searchedAt");
          final int _cursorIndexOfResultCount = CursorUtil.getColumnIndexOrThrow(_cursor, "resultCount");
          final List<SearchHistoryEntity> _result = new ArrayList<SearchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SearchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpKeyword;
            _tmpKeyword = _cursor.getString(_cursorIndexOfKeyword);
            final String _tmpSearchType;
            _tmpSearchType = _cursor.getString(_cursorIndexOfSearchType);
            final long _tmpSearchedAt;
            _tmpSearchedAt = _cursor.getLong(_cursorIndexOfSearchedAt);
            final Integer _tmpResultCount;
            if (_cursor.isNull(_cursorIndexOfResultCount)) {
              _tmpResultCount = null;
            } else {
              _tmpResultCount = _cursor.getInt(_cursorIndexOfResultCount);
            }
            _item = new SearchHistoryEntity(_tmpId,_tmpKeyword,_tmpSearchType,_tmpSearchedAt,_tmpResultCount);
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
  public Flow<List<SearchHistoryEntity>> getRecentSearchesByType(final String searchType,
      final int limit) {
    final String _sql = "SELECT * FROM search_history WHERE searchType = ? ORDER BY searchedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, searchType);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"search_history"}, new Callable<List<SearchHistoryEntity>>() {
      @Override
      @NonNull
      public List<SearchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "keyword");
          final int _cursorIndexOfSearchType = CursorUtil.getColumnIndexOrThrow(_cursor, "searchType");
          final int _cursorIndexOfSearchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "searchedAt");
          final int _cursorIndexOfResultCount = CursorUtil.getColumnIndexOrThrow(_cursor, "resultCount");
          final List<SearchHistoryEntity> _result = new ArrayList<SearchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SearchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpKeyword;
            _tmpKeyword = _cursor.getString(_cursorIndexOfKeyword);
            final String _tmpSearchType;
            _tmpSearchType = _cursor.getString(_cursorIndexOfSearchType);
            final long _tmpSearchedAt;
            _tmpSearchedAt = _cursor.getLong(_cursorIndexOfSearchedAt);
            final Integer _tmpResultCount;
            if (_cursor.isNull(_cursorIndexOfResultCount)) {
              _tmpResultCount = null;
            } else {
              _tmpResultCount = _cursor.getInt(_cursorIndexOfResultCount);
            }
            _item = new SearchHistoryEntity(_tmpId,_tmpKeyword,_tmpSearchType,_tmpSearchedAt,_tmpResultCount);
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
  public Object searchByKeyword(final String query, final int limit,
      final Continuation<? super List<SearchHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM search_history WHERE keyword LIKE ? || '%' ORDER BY searchedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SearchHistoryEntity>>() {
      @Override
      @NonNull
      public List<SearchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "keyword");
          final int _cursorIndexOfSearchType = CursorUtil.getColumnIndexOrThrow(_cursor, "searchType");
          final int _cursorIndexOfSearchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "searchedAt");
          final int _cursorIndexOfResultCount = CursorUtil.getColumnIndexOrThrow(_cursor, "resultCount");
          final List<SearchHistoryEntity> _result = new ArrayList<SearchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SearchHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpKeyword;
            _tmpKeyword = _cursor.getString(_cursorIndexOfKeyword);
            final String _tmpSearchType;
            _tmpSearchType = _cursor.getString(_cursorIndexOfSearchType);
            final long _tmpSearchedAt;
            _tmpSearchedAt = _cursor.getLong(_cursorIndexOfSearchedAt);
            final Integer _tmpResultCount;
            if (_cursor.isNull(_cursorIndexOfResultCount)) {
              _tmpResultCount = null;
            } else {
              _tmpResultCount = _cursor.getInt(_cursorIndexOfResultCount);
            }
            _item = new SearchHistoryEntity(_tmpId,_tmpKeyword,_tmpSearchType,_tmpSearchedAt,_tmpResultCount);
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
  public Object getDistinctKeywords(final int limit,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT keyword FROM search_history ORDER BY searchedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
