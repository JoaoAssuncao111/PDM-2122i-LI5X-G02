package pt.isel.pdm.chess4android.daily


import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import pt.isel.pdm.chess4android.common.DailyPuzzleApplication
import pt.isel.pdm.chess4android.common.PuzzleInfoRepository

class DownloadDailyPuzzle (appContext: Context, workerParams: WorkerParameters)
    : ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        val app : DailyPuzzleApplication = applicationContext as DailyPuzzleApplication
        val repo = PuzzleInfoRepository(app.dailyPuzzleService, app.historyDB.getPuzzleHistoryDao())

        return CallbackToFutureAdapter.getFuture { completer ->
            repo.fetchDailyPuzzle(mustSaveToDB = true) { result ->
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