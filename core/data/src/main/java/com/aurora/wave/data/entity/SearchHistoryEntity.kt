package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 搜索历史实体
 * 
 * 记录用户的搜索历史，支持快速重复搜索。
 */
@Entity(
    tableName = "search_history",
    indices = [
        Index(value = ["keyword"]),
        Index(value = ["searchType"]),
        Index(value = ["searchedAt"])
    ]
)
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** 搜索关键词 */
    val keyword: String,
    
    /** 搜索类型：MESSAGE, CONTACT, CONVERSATION, ALL */
    val searchType: String = "ALL",
    
    /** 搜索时间 */
    val searchedAt: Long = System.currentTimeMillis(),
    
    /** 搜索结果数量（可选） */
    val resultCount: Int? = null
)
