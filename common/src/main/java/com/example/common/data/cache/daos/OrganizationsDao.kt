package com.example.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.common.data.cache.module.cachedorganization.CachedOrganization

@Dao
interface OrganizationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(organizations: List<CachedOrganization>)
}