package com.aurora.wave.di

import android.content.Context
import androidx.room.Room
import com.aurora.wave.data.dao.ConversationDao
import com.aurora.wave.data.dao.DraftDao
import com.aurora.wave.data.dao.MediaFileDao
import com.aurora.wave.data.dao.MessageDao
import com.aurora.wave.data.dao.PinnedMessageDao
import com.aurora.wave.data.dao.ReactionDao
import com.aurora.wave.data.dao.SearchHistoryDao
import com.aurora.wave.data.dao.SyncStateDao
import com.aurora.wave.data.dao.UserDao
import com.aurora.wave.data.database.WaveDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 数据库依赖注入模块
 * 
 * 提供 WaveDatabase 实例和所有 DAO。
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WaveDatabase {
        return Room.databaseBuilder(
            context,
            WaveDatabase::class.java,
            WaveDatabase.DATABASE_NAME
        )
            // 开发阶段使用破坏性迁移，生产环境需要写正确的迁移脚本
            .fallbackToDestructiveMigration()
            .build()
    }
    
    // ==================== 核心 DAO ====================
    
    @Provides
    fun provideMessageDao(database: WaveDatabase): MessageDao {
        return database.messageDao()
    }
    
    @Provides
    fun provideConversationDao(database: WaveDatabase): ConversationDao {
        return database.conversationDao()
    }
    
    @Provides
    fun provideUserDao(database: WaveDatabase): UserDao {
        return database.userDao()
    }
    
    // ==================== 扩展 DAO ====================
    
    @Provides
    fun provideMediaFileDao(database: WaveDatabase): MediaFileDao {
        return database.mediaFileDao()
    }
    
    @Provides
    fun provideSyncStateDao(database: WaveDatabase): SyncStateDao {
        return database.syncStateDao()
    }
    
    @Provides
    fun provideDraftDao(database: WaveDatabase): DraftDao {
        return database.draftDao()
    }
    
    @Provides
    fun provideReactionDao(database: WaveDatabase): ReactionDao {
        return database.reactionDao()
    }
    
    @Provides
    fun providePinnedMessageDao(database: WaveDatabase): PinnedMessageDao {
        return database.pinnedMessageDao()
    }
    
    @Provides
    fun provideSearchHistoryDao(database: WaveDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}
