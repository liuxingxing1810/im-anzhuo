package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE isContact = 1 ORDER BY displayName COLLATE NOCASE ASC")
    fun getContactsFlow(): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE isContact = 1 AND (displayName LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR remark LIKE '%' || :query || '%') ORDER BY displayName COLLATE NOCASE ASC")
    fun searchContacts(query: String): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserFlow(userId: String): Flow<UserEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
    
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("UPDATE users SET isContact = :isContact WHERE id = :userId")
    suspend fun setContactStatus(userId: String, isContact: Boolean)
    
    @Query("UPDATE users SET isBlocked = :isBlocked WHERE id = :userId")
    suspend fun setBlockedStatus(userId: String, isBlocked: Boolean)
    
    @Query("UPDATE users SET remark = :remark WHERE id = :userId")
    suspend fun setRemark(userId: String, remark: String?)
    
    @Query("UPDATE users SET isOnline = :isOnline, lastSeenAt = :lastSeenAt WHERE id = :userId")
    suspend fun updateOnlineStatus(userId: String, isOnline: Boolean, lastSeenAt: Long?)
    
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
    
    @Query("SELECT COUNT(*) FROM users WHERE isContact = 1")
    fun getContactCount(): Flow<Int>
}
