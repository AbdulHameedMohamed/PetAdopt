package com.example.common.data.cache.module.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class CachedTag(
    @PrimaryKey
    val tag: String
)