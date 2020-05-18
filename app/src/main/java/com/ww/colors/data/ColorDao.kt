package com.ww.colors.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ColorDao {

    @Query("SELECT * FROM colors ORDER BY id")
    fun getColors(): LiveData<List<Color>>

    @Query("SELECT * FROM colors WHERE id= :colorId")
    fun getColor(colorId: String): LiveData<Color>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(colors: List<Color>)
}