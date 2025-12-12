package com.aurora.wave.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aurora.wave.data.converter.Converters
import com.aurora.wave.data.dao.ConversationDao
import com.aurora.wave.data.dao.DraftDao
import com.aurora.wave.data.dao.MediaFileDao
import com.aurora.wave.data.dao.MessageDao
import com.aurora.wave.data.dao.PinnedMessageDao
import com.aurora.wave.data.dao.ReactionDao
import com.aurora.wave.data.dao.SearchHistoryDao
import com.aurora.wave.data.dao.SyncStateDao
import com.aurora.wave.data.dao.UserDao
import com.aurora.wave.data.entity.ConversationEntity
import com.aurora.wave.data.entity.DraftEntity
import com.aurora.wave.data.entity.MediaFileEntity
import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.entity.PinnedMessageEntity
import com.aurora.wave.data.entity.ReactionEntity
import com.aurora.wave.data.entity.SearchHistoryEntity
import com.aurora.wave.data.entity.SyncStateEntity
import com.aurora.wave.data.entity.UserEntity

/**
 * 星星IM 本地数据库
 * 
 * 表结构：
 * - messages: 消息记录
 * - conversations: 会话列表
 * - users: 用户信息
 * - media_files: 媒体文件索引
 * - sync_state: 同步状态
 * - drafts: 草稿
 * - reactions: 消息反应
 * - pinned_messages: 置顶消息
 * - search_history: 搜索历史
 */
@Database(
    entities = [
        // 核心表
        MessageEntity::class,
        ConversationEntity::class,
        UserEntity::class,
        // 扩展表
        MediaFileEntity::class,
        SyncStateEntity::class,
        DraftEntity::class,
        ReactionEntity::class,
        PinnedMessageEntity::class,
        SearchHistoryEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WaveDatabase : RoomDatabase() {
    
    // 核心 DAO
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
    abstract fun userDao(): UserDao
    
    // 扩展 DAO
    abstract fun mediaFileDao(): MediaFileDao
    abstract fun syncStateDao(): SyncStateDao
    abstract fun draftDao(): DraftDao
    abstract fun reactionDao(): ReactionDao
    abstract fun pinnedMessageDao(): PinnedMessageDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    
    companion object {
        const val DATABASE_NAME = "wave_database"
    }
}
