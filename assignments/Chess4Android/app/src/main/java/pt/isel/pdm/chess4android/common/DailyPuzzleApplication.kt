package pt.isel.pdm.chess4android.common

import android.app.Application
import androidx.room.Room
import androidx.work.*
import pt.isel.pdm.chess4android.DailyPuzzleInfoService
import pt.isel.pdm.chess4android.daily.DownloadDailyPuzzle
import pt.isel.pdm.chess4android.history.HistoryDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DailyPuzzleApplication : Application() {


    val dailyPuzzleInfoService: DailyPuzzleInfoService by lazy {
        Retrofit.Builder()
            .baseUrl("https://lichess.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleInfoService::class.java)
    }

    val historyDB: HistoryDatabase by lazy {
        Room
            //.inMemoryDatabaseBuilder(this, HistoryDatabase::class.java)
           .databaseBuilder(this, HistoryDatabase::class.java, "history_db")
            .build()
    }
    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<DownloadDailyPuzzle>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "DownloadDailyPuzzle",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}

