package com.aurora.wave.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aurora.wave.data.converter.Converters
import com.aurora.wave.data.dao.ConversationDao
import com.aurora.wave.data.dao.MessageDao
import com.aurora.wave.data.dao.UserDao
import com.aurora.wave.data.entity.ConversationEntity
import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.entity.UserEntity

@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WaveDatabase : RoomDatabase() {
    
    abstract fun messageDao(): MessageDao
    
    abstract fun conversationDao(): ConversationDao
    
    abstract fun userDao(): UserDao
    
    companion object {
        const val DATABASE_NAME = "wave_database"
    }
}
