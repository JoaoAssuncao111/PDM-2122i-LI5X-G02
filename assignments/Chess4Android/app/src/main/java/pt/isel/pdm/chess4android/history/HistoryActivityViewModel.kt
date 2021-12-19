package pt.isel.pdm.chess4android.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.chess4android.DailyPuzzleInfoDTO
import pt.isel.pdm.chess4android.PuzzleInfoDTO
import pt.isel.pdm.chess4android.callbackAfterAsync
import pt.isel.pdm.chess4android.common.DailyPuzzleApplication

class HistoryActivityViewModel (
    application: Application,
) : AndroidViewModel(application) {

    var history: LiveData<List<DailyPuzzleInfoDTO>>? = null
        private set
    private val historyDao: PuzzleHistoryDao by lazy {
        getApplication<DailyPuzzleApplication>().historyDB.getPuzzleHistoryDao()
    }

    fun loadHistory(): LiveData<List<DailyPuzzleInfoDTO>> {
        val publish = MutableLiveData<List<DailyPuzzleInfoDTO>>()
        history = publish
        callbackAfterAsync(
            asyncAction = {
                //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Getting history from local DB")
                historyDao.getAll().map {
                    DailyPuzzleInfoDTO(
                        puzzleInfoDTO = PuzzleInfoDTO(it.game, it.puzzle),
                        date = it.timestamp.toString(),
                        state = it.state
                    )
                }
            },
            callback = { result ->
                //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: mapping results")
                result.onSuccess { publish.value = it }
                result.onFailure { publish.value = emptyList() }
            }
        )
        return publish
    }
}