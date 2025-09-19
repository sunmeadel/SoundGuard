package com.example.soundguard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "gmail") val gmail: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false,
    @ColumnInfo(name = "server_id") val serverId: String? = null
)