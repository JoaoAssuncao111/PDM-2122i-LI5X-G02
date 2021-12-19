package pt.isel.pdm.chess4android.common

import android.app.Application
import androidx.room.Room
import androidx.work.*
import pt.isel.pdm.chess4android.DailyPuzzleService
import pt.isel.pdm.chess4android.daily.DownloadDailyPuzzle
import pt.isel.pdm.chess4android.history.HistoryDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DailyPuzzleApplication : Application() {
    init {

    }

    val dailyPuzzleService: DailyPuzzleService by lazy {
        Retrofit.Builder()
            .baseUrl("https://lichess.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }

    val historyDB: HistoryDatabase by lazy {
        Room
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

