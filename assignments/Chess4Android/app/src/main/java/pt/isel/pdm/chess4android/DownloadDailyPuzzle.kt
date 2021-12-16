package pt.isel.pdm.chess4android


import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture

class DownloadDailyPuzzle (appContext: Context, workerParams: WorkerParameters)
    : ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        val app : DailyPuzzleApplication = applicationContext as DailyPuzzleApplication
        val repo = QuoteOfDayRepository(app.dailyPuzzleService, app.historyDB.getPuzzleHistoryDao())

        return CallbackToFutureAdapter.getFuture { completer ->
            repo.fetchQuoteOfDay(mustSaveToDB = true) { result ->
                result
                    .onSuccess {

                        completer.set(Result.success())
                    }
                    .onFailure {

                        completer.setException(it)
                    }
            }
        }
    }

}