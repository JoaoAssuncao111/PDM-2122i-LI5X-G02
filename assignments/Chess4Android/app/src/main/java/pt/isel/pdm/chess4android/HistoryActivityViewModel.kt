package pt.isel.pdm.chess4android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.chess4android.DailyPuzzleService

class HistoryActivityViewModel (
    application: Application,
) : AndroidViewModel(application) {

    var history: LiveData<List<DailyPuzzleInfoDTO>>? = null
        private set
    private val historyDao: PuzzleHistoryDao by lazy {
        getApplication<Puzz>
    }

}