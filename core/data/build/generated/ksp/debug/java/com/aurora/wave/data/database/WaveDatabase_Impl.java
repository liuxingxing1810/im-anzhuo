package com.aurora.wave.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.aurora.wave.data.dao.ConversationDao;
import com.aurora.wave.data.dao.ConversationDao_Impl;
import com.aurora.wave.data.dao.DraftDao;
import com.aurora.wave.data.dao.DraftDao_Impl;
import com.aurora.wave.data.dao.MediaFileDao;
import com.aurora.wave.data.dao.MediaFileDao_Impl;
import com.aurora.wave.data.dao.MessageDao;
import com.aurora.wave.data.dao.MessageDao_Impl;
import com.aurora.wave.data.dao.PinnedMessageDao;
import com.aurora.wave.data.dao.PinnedMessageDao_Impl;
import com.aurora.wave.data.dao.ReactionDao;
import com.aurora.wave.data.dao.ReactionDao_Impl;
import com.aurora.wave.data.dao.SearchHistoryDao;
import com.aurora.wave.data.dao.SearchHistoryDao_Impl;
import com.aurora.wave.data.dao.SyncStateDao;
import com.aurora.wave.data.dao.SyncStateDao_Impl;
import com.aurora.wave.data.dao.UserDao;
import com.aurora.wave.data.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WaveDatabase_Impl extends WaveDatabase {
  private volatile MessageDao _messageDao;

  private volatile ConversationDao _conversationDao;

  private volatile UserDao _userDao;

  private volatile MediaFileDao _mediaFileDao;

  private volatile SyncStateDao _syncStateDao;

  private volatile DraftDao _draftDao;

  private volatile ReactionDao _reactionDao;

  private volatile PinnedMessageDao _pinnedMessageDao;

  private volatile SearchHistoryDao _searchHistoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` TEXT NOT NULL, `conversationId` TEXT NOT NULL, `senderId` TEXT NOT NULL, `senderName` TEXT, `senderAvatar` TEXT, `type` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `deliveryStatus` TEXT NOT NULL, `replyToMessageId` TEXT, `forwardedFromId` TEXT, `isOutgoing` INTEGER NOT NULL, `isEdited` INTEGER NOT NULL, `editedAt` INTEGER, `mentions` TEXT, `reactions` TEXT, `isDeleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `conversations` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `name` TEXT NOT NULL, `avatarUrl` TEXT, `lastMessage` TEXT, `lastMessageTimestamp` INTEGER NOT NULL, `unreadCount` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `isMuted` INTEGER NOT NULL, `draft` TEXT, `isArchived` INTEGER NOT NULL, `participantIds` TEXT NOT NULL, `memberCount` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `displayName` TEXT NOT NULL, `avatarUrl` TEXT, `username` TEXT, `statusMessage` TEXT, `phoneNumber` TEXT, `isContact` INTEGER NOT NULL, `isBlocked` INTEGER NOT NULL, `remark` TEXT, `tags` TEXT, `lastSeenAt` INTEGER, `isOnline` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_files` (`id` TEXT NOT NULL, `messageId` TEXT NOT NULL, `conversationId` TEXT NOT NULL, `type` TEXT NOT NULL, `fileName` TEXT NOT NULL, `fileSize` INTEGER NOT NULL, `mimeType` TEXT NOT NULL, `localPath` TEXT, `remotePath` TEXT NOT NULL, `thumbnailPath` TEXT, `thumbnailRemotePath` TEXT, `downloadStatus` TEXT NOT NULL, `uploadStatus` TEXT NOT NULL, `progress` INTEGER NOT NULL, `width` INTEGER, `height` INTEGER, `duration` INTEGER, `md5Hash` TEXT, `createdAt` INTEGER NOT NULL, `lastAccessedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`messageId`) REFERENCES `messages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_files_messageId` ON `media_files` (`messageId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_files_conversationId` ON `media_files` (`conversationId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_files_type` ON `media_files` (`type`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_files_downloadStatus` ON `media_files` (`downloadStatus`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sync_state` (`conversationId` TEXT NOT NULL, `lastSyncTimestamp` INTEGER NOT NULL, `serverSeq` INTEGER, `localSeq` INTEGER, `syncStatus` TEXT NOT NULL, `lastSyncedMessageId` TEXT, `failureCount` INTEGER NOT NULL, `lastError` TEXT, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`conversationId`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sync_state_syncStatus` ON `sync_state` (`syncStatus`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `drafts` (`conversationId` TEXT NOT NULL, `content` TEXT NOT NULL, `replyToMessageId` TEXT, `mentionUserIds` TEXT, `attachmentIds` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`conversationId`), FOREIGN KEY(`conversationId`) REFERENCES `conversations`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_drafts_conversationId` ON `drafts` (`conversationId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reactions` (`messageId` TEXT NOT NULL, `userId` TEXT NOT NULL, `emoji` TEXT NOT NULL, `userName` TEXT, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`messageId`, `userId`, `emoji`), FOREIGN KEY(`messageId`) REFERENCES `messages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reactions_messageId` ON `reactions` (`messageId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reactions_userId` ON `reactions` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pinned_messages` (`conversationId` TEXT NOT NULL, `messageId` TEXT NOT NULL, `pinnedByUserId` TEXT NOT NULL, `pinnedByUserName` TEXT, `pinnedAt` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, PRIMARY KEY(`conversationId`, `messageId`), FOREIGN KEY(`conversationId`) REFERENCES `conversations`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`messageId`) REFERENCES `messages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pinned_messages_conversationId` ON `pinned_messages` (`conversationId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pinned_messages_messageId` ON `pinned_messages` (`messageId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `search_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `keyword` TEXT NOT NULL, `searchType` TEXT NOT NULL, `searchedAt` INTEGER NOT NULL, `resultCount` INTEGER)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_search_history_keyword` ON `search_history` (`keyword`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_search_history_searchType` ON `search_history` (`searchType`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_search_history_searchedAt` ON `search_history` (`searchedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '58f24a01730093e478edb6b207a0e72d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `conversations`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `media_files`");
        db.execSQL("DROP TABLE IF EXISTS `sync_state`");
        db.execSQL("DROP TABLE IF EXISTS `drafts`");
        db.execSQL("DROP TABLE IF EXISTS `reactions`");
        db.execSQL("DROP TABLE IF EXISTS `pinned_messages`");
        db.execSQL("DROP TABLE IF EXISTS `search_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(17);
        _columnsMessages.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("conversationId", new TableInfo.Column("conversationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderId", new TableInfo.Column("senderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderName", new TableInfo.Column("senderName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("senderAvatar", new TableInfo.Column("senderAvatar", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("deliveryStatus", new TableInfo.Column("deliveryStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("replyToMessageId", new TableInfo.Column("replyToMessageId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("forwardedFromId", new TableInfo.Column("forwardedFromId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isOutgoing", new TableInfo.Column("isOutgoing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isEdited", new TableInfo.Column("isEdited", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("editedAt", new TableInfo.Column("editedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("mentions", new TableInfo.Column("mentions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("reactions", new TableInfo.Column("reactions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.aurora.wave.data.entity.MessageEntity).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsConversations = new HashMap<String, TableInfo.Column>(15);
        _columnsConversations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("avatarUrl", new TableInfo.Column("avatarUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("lastMessage", new TableInfo.Column("lastMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("lastMessageTimestamp", new TableInfo.Column("lastMessageTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("unreadCount", new TableInfo.Column("unreadCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("isPinned", new TableInfo.Column("isPinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("isMuted", new TableInfo.Column("isMuted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("draft", new TableInfo.Column("draft", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("participantIds", new TableInfo.Column("participantIds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("memberCount", new TableInfo.Column("memberCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConversations.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConversations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesConversations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoConversations = new TableInfo("conversations", _columnsConversations, _foreignKeysConversations, _indicesConversations);
        final TableInfo _existingConversations = TableInfo.read(db, "conversations");
        if (!_infoConversations.equals(_existingConversations)) {
          return new RoomOpenHelper.ValidationResult(false, "conversations(com.aurora.wave.data.entity.ConversationEntity).\n"
                  + " Expected:\n" + _infoConversations + "\n"
                  + " Found:\n" + _existingConversations);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(12);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("avatarUrl", new TableInfo.Column("avatarUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("statusMessage", new TableInfo.Column("statusMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isContact", new TableInfo.Column("isContact", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isBlocked", new TableInfo.Column("isBlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("remark", new TableInfo.Column("remark", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("tags", new TableInfo.Column("tags", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("lastSeenAt", new TableInfo.Column("lastSeenAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isOnline", new TableInfo.Column("isOnline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.aurora.wave.data.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsMediaFiles = new HashMap<String, TableInfo.Column>(20);
        _columnsMediaFiles.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("messageId", new TableInfo.Column("messageId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("conversationId", new TableInfo.Column("conversationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("localPath", new TableInfo.Column("localPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("remotePath", new TableInfo.Column("remotePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("thumbnailPath", new TableInfo.Column("thumbnailPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("thumbnailRemotePath", new TableInfo.Column("thumbnailRemotePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("downloadStatus", new TableInfo.Column("downloadStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("uploadStatus", new TableInfo.Column("uploadStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("width", new TableInfo.Column("width", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("height", new TableInfo.Column("height", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("duration", new TableInfo.Column("duration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("md5Hash", new TableInfo.Column("md5Hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaFiles.put("lastAccessedAt", new TableInfo.Column("lastAccessedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaFiles = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMediaFiles.add(new TableInfo.ForeignKey("messages", "CASCADE", "NO ACTION", Arrays.asList("messageId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMediaFiles = new HashSet<TableInfo.Index>(4);
        _indicesMediaFiles.add(new TableInfo.Index("index_media_files_messageId", false, Arrays.asList("messageId"), Arrays.asList("ASC")));
        _indicesMediaFiles.add(new TableInfo.Index("index_media_files_conversationId", false, Arrays.asList("conversationId"), Arrays.asList("ASC")));
        _indicesMediaFiles.add(new TableInfo.Index("index_media_files_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        _indicesMediaFiles.add(new TableInfo.Index("index_media_files_downloadStatus", false, Arrays.asList("downloadStatus"), Arrays.asList("ASC")));
        final TableInfo _infoMediaFiles = new TableInfo("media_files", _columnsMediaFiles, _foreignKeysMediaFiles, _indicesMediaFiles);
        final TableInfo _existingMediaFiles = TableInfo.read(db, "media_files");
        if (!_infoMediaFiles.equals(_existingMediaFiles)) {
          return new RoomOpenHelper.ValidationResult(false, "media_files(com.aurora.wave.data.entity.MediaFileEntity).\n"
                  + " Expected:\n" + _infoMediaFiles + "\n"
                  + " Found:\n" + _existingMediaFiles);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncState = new HashMap<String, TableInfo.Column>(9);
        _columnsSyncState.put("conversationId", new TableInfo.Column("conversationId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("lastSyncTimestamp", new TableInfo.Column("lastSyncTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("serverSeq", new TableInfo.Column("serverSeq", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("localSeq", new TableInfo.Column("localSeq", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("lastSyncedMessageId", new TableInfo.Column("lastSyncedMessageId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("failureCount", new TableInfo.Column("failureCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("lastError", new TableInfo.Column("lastError", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSyncState = new HashSet<TableInfo.Index>(1);
        _indicesSyncState.add(new TableInfo.Index("index_sync_state_syncStatus", false, Arrays.asList("syncStatus"), Arrays.asList("ASC")));
        final TableInfo _infoSyncState = new TableInfo("sync_state", _columnsSyncState, _foreignKeysSyncState, _indicesSyncState);
        final TableInfo _existingSyncState = TableInfo.read(db, "sync_state");
        if (!_infoSyncState.equals(_existingSyncState)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_state(com.aurora.wave.data.entity.SyncStateEntity).\n"
                  + " Expected:\n" + _infoSyncState + "\n"
                  + " Found:\n" + _existingSyncState);
        }
        final HashMap<String, TableInfo.Column> _columnsDrafts = new HashMap<String, TableInfo.Column>(7);
        _columnsDrafts.put("conversationId", new TableInfo.Column("conversationId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("replyToMessageId", new TableInfo.Column("replyToMessageId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("mentionUserIds", new TableInfo.Column("mentionUserIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("attachmentIds", new TableInfo.Column("attachmentIds", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrafts.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDrafts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysDrafts.add(new TableInfo.ForeignKey("conversations", "CASCADE", "NO ACTION", Arrays.asList("conversationId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDrafts = new HashSet<TableInfo.Index>(1);
        _indicesDrafts.add(new TableInfo.Index("index_drafts_conversationId", true, Arrays.asList("conversationId"), Arrays.asList("ASC")));
        final TableInfo _infoDrafts = new TableInfo("drafts", _columnsDrafts, _foreignKeysDrafts, _indicesDrafts);
        final TableInfo _existingDrafts = TableInfo.read(db, "drafts");
        if (!_infoDrafts.equals(_existingDrafts)) {
          return new RoomOpenHelper.ValidationResult(false, "drafts(com.aurora.wave.data.entity.DraftEntity).\n"
                  + " Expected:\n" + _infoDrafts + "\n"
                  + " Found:\n" + _existingDrafts);
        }
        final HashMap<String, TableInfo.Column> _columnsReactions = new HashMap<String, TableInfo.Column>(5);
        _columnsReactions.put("messageId", new TableInfo.Column("messageId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReactions.put("userId", new TableInfo.Column("userId", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReactions.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReactions.put("userName", new TableInfo.Column("userName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReactions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReactions.add(new TableInfo.ForeignKey("messages", "CASCADE", "NO ACTION", Arrays.asList("messageId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReactions = new HashSet<TableInfo.Index>(2);
        _indicesReactions.add(new TableInfo.Index("index_reactions_messageId", false, Arrays.asList("messageId"), Arrays.asList("ASC")));
        _indicesReactions.add(new TableInfo.Index("index_reactions_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoReactions = new TableInfo("reactions", _columnsReactions, _foreignKeysReactions, _indicesReactions);
        final TableInfo _existingReactions = TableInfo.read(db, "reactions");
        if (!_infoReactions.equals(_existingReactions)) {
          return new RoomOpenHelper.ValidationResult(false, "reactions(com.aurora.wave.data.entity.ReactionEntity).\n"
                  + " Expected:\n" + _infoReactions + "\n"
                  + " Found:\n" + _existingReactions);
        }
        final HashMap<String, TableInfo.Column> _columnsPinnedMessages = new HashMap<String, TableInfo.Column>(6);
        _columnsPinnedMessages.put("conversationId", new TableInfo.Column("conversationId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinnedMessages.put("messageId", new TableInfo.Column("messageId", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinnedMessages.put("pinnedByUserId", new TableInfo.Column("pinnedByUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinnedMessages.put("pinnedByUserName", new TableInfo.Column("pinnedByUserName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinnedMessages.put("pinnedAt", new TableInfo.Column("pinnedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinnedMessages.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPinnedMessages = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysPinnedMessages.add(new TableInfo.ForeignKey("conversations", "CASCADE", "NO ACTION", Arrays.asList("conversationId"), Arrays.asList("id")));
        _foreignKeysPinnedMessages.add(new TableInfo.ForeignKey("messages", "CASCADE", "NO ACTION", Arrays.asList("messageId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPinnedMessages = new HashSet<TableInfo.Index>(2);
        _indicesPinnedMessages.add(new TableInfo.Index("index_pinned_messages_conversationId", false, Arrays.asList("conversationId"), Arrays.asList("ASC")));
        _indicesPinnedMessages.add(new TableInfo.Index("index_pinned_messages_messageId", false, Arrays.asList("messageId"), Arrays.asList("ASC")));
        final TableInfo _infoPinnedMessages = new TableInfo("pinned_messages", _columnsPinnedMessages, _foreignKeysPinnedMessages, _indicesPinnedMessages);
        final TableInfo _existingPinnedMessages = TableInfo.read(db, "pinned_messages");
        if (!_infoPinnedMessages.equals(_existingPinnedMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "pinned_messages(com.aurora.wave.data.entity.PinnedMessageEntity).\n"
                  + " Expected:\n" + _infoPinnedMessages + "\n"
                  + " Found:\n" + _existingPinnedMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsSearchHistory = new HashMap<String, TableInfo.Column>(5);
        _columnsSearchHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("keyword", new TableInfo.Column("keyword", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("searchType", new TableInfo.Column("searchType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("searchedAt", new TableInfo.Column("searchedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSearchHistory.put("resultCount", new TableInfo.Column("resultCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSearchHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSearchHistory = new HashSet<TableInfo.Index>(3);
        _indicesSearchHistory.add(new TableInfo.Index("index_search_history_keyword", false, Arrays.asList("keyword"), Arrays.asList("ASC")));
        _indicesSearchHistory.add(new TableInfo.Index("index_search_history_searchType", false, Arrays.asList("searchType"), Arrays.asList("ASC")));
        _indicesSearchHistory.add(new TableInfo.Index("index_search_history_searchedAt", false, Arrays.asList("searchedAt"), Arrays.asList("ASC")));
        final TableInfo _infoSearchHistory = new TableInfo("search_history", _columnsSearchHistory, _foreignKeysSearchHistory, _indicesSearchHistory);
        final TableInfo _existingSearchHistory = TableInfo.read(db, "search_history");
        if (!_infoSearchHistory.equals(_existingSearchHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "search_history(com.aurora.wave.data.entity.SearchHistoryEntity).\n"
                  + " Expected:\n" + _infoSearchHistory + "\n"
                  + " Found:\n" + _existingSearchHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "58f24a01730093e478edb6b207a0e72d", "387692d1c9cc787bce0021b8328a977e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "messages","conversations","users","media_files","sync_state","drafts","reactions","pinned_messages","search_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `conversations`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `media_files`");
      _db.execSQL("DELETE FROM `sync_state`");
      _db.execSQL("DELETE FROM `drafts`");
      _db.execSQL("DELETE FROM `reactions`");
      _db.execSQL("DELETE FROM `pinned_messages`");
      _db.execSQL("DELETE FROM `search_history`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConversationDao.class, ConversationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MediaFileDao.class, MediaFileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncStateDao.class, SyncStateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DraftDao.class, DraftDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReactionDao.class, ReactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PinnedMessageDao.class, PinnedMessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SearchHistoryDao.class, SearchHistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MessageDao messageDao() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }

  @Override
  public ConversationDao conversationDao() {
    if (_conversationDao != null) {
      return _conversationDao;
    } else {
      synchronized(this) {
        if(_conversationDao == null) {
          _conversationDao = new ConversationDao_Impl(this);
        }
        return _conversationDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public MediaFileDao mediaFileDao() {
    if (_mediaFileDao != null) {
      return _mediaFileDao;
    } else {
      synchronized(this) {
        if(_mediaFileDao == null) {
          _mediaFileDao = new MediaFileDao_Impl(this);
        }
        return _mediaFileDao;
      }
    }
  }

  @Override
  public SyncStateDao syncStateDao() {
    if (_syncStateDao != null) {
      return _syncStateDao;
    } else {
      synchronized(this) {
        if(_syncStateDao == null) {
          _syncStateDao = new SyncStateDao_Impl(this);
        }
        return _syncStateDao;
      }
    }
  }

  @Override
  public DraftDao draftDao() {
    if (_draftDao != null) {
      return _draftDao;
    } else {
      synchronized(this) {
        if(_draftDao == null) {
          _draftDao = new DraftDao_Impl(this);
        }
        return _draftDao;
      }
    }
  }

  @Override
  public ReactionDao reactionDao() {
    if (_reactionDao != null) {
      return _reactionDao;
    } else {
      synchronized(this) {
        if(_reactionDao == null) {
          _reactionDao = new ReactionDao_Impl(this);
        }
        return _reactionDao;
      }
    }
  }

  @Override
  public PinnedMessageDao pinnedMessageDao() {
    if (_pinnedMessageDao != null) {
      return _pinnedMessageDao;
    } else {
      synchronized(this) {
        if(_pinnedMessageDao == null) {
          _pinnedMessageDao = new PinnedMessageDao_Impl(this);
        }
        return _pinnedMessageDao;
      }
    }
  }

  @Override
  public SearchHistoryDao searchHistoryDao() {
    if (_searchHistoryDao != null) {
      return _searchHistoryDao;
    } else {
      synchronized(this) {
        if(_searchHistoryDao == null) {
          _searchHistoryDao = new SearchHistoryDao_Impl(this);
        }
        return _searchHistoryDao;
      }
    }
  }
}
