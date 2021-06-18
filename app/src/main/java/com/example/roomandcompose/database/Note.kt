package com.example.roomandcompose.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomandcompose.utils.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class Note(
    @ColumnInfo(name = Constants.TITLE)
    var title : String,

    @ColumnInfo(name = Constants.NOTE)
    var note : String,

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
)
