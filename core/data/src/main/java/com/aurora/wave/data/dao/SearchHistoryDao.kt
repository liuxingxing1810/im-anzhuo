package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aurora.wave.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史 DAO
 * 
 * 管理用户的搜索历史记录。
 */
@Dao
interface SearchHistoryDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 20): Flow<List<SearchHistoryEntity>>
    
    @Query("SELECT * FROM search_history WHERE searchType = :searchType ORDER BY searchedAt DESC LIMIT :limit")
    fun getRecentSearchesByType(searchType: String, limit: Int = 20): Flow<List<SearchHistoryEntity>>
    
    @Query("SELECT * FROM search_history WHERE keyword LIKE :query || '%' ORDER BY searchedAt DESC LIMIT :limit")
    suspend fun searchByKeyword(query: String, limit: Int = 10): List<SearchHistoryEntity>
    
    @Query("SELECT DISTINCT keyword FROM search_history ORDER BY searchedAt DESC LIMIT :limit")
    suspend fun getDistinctKeywords(limit: Int = 20): List<String>
    
    // ==================== 插入操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistoryEntity)
    
    /** 插入或更新搜索记录（更新搜索时间） */
    @Query("UPDATE search_history SET searchedAt = :timestamp, resultCount = :resultCount WHERE keyword = :keyword AND searchType = :searchType")
    suspend fun updateSearchTime(keyword: String, searchType: String, timestamp: Long = System.currentTimeMillis(), resultCount: Int? = null)
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM search_history WHERE keyword = :keyword")
    suspend fun deleteByKeyword(keyword: String)
    
    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
    
    /** 保留最近N条，删除旧记录 */
    @Query("DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY searchedAt DESC LIMIT :keepCount)")
    suspend fun keepRecentOnly(keepCount: Int = 100)
}
