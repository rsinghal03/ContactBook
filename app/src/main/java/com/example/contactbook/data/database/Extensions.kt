package com.example.contactbook.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey

@Entity(tableName = "Extensions", primaryKeys = ["_context", "_id"])
data class Extensions(
    val _context: String,
    val _id: Int)