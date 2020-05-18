package com.ww.colors.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.ww.colors.data.AppDatabase
import com.ww.colors.data.Color
import com.ww.colors.utilities.COLOR_DATA_FILENAME
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class SeedDatabaseWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(COLOR_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val colorType = object : TypeToken<List<Color>>() {}.type
                    val colorList: List<Color> = Gson().fromJson(jsonReader, colorType)
                    val database = AppDatabase.getInstance(applicationContext)
                    database.colorDao().insertAll(colorList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = SeedDatabaseWorker::class.java.simpleName
    }
}
