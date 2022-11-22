package com.example.tripplanner.data.access.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tag", indices = [Index(value = ["tag_id"])])
data class TagEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "tag_id") val tagId: Int = 0,
    @ColumnInfo(name = "tag_name") val tagName: String)