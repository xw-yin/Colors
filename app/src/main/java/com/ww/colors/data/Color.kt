package com.ww.colors.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
data class Color(
    @PrimaryKey @ColumnInfo(name = "id") val colorId: String,
    val b: Int,
    val c: Int,
    val g: Int,
    val k: Int,
    val m: Int,
    val r: Int,
    val y: Int,
    val jpName: String,
    val colorHex: String,
    val enName: String
)