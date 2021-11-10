package pt.isel.pdm.chess4android

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private const val MAIN_ACTIVITY_VIEW_STATE = "MainActivity.ViewState"

class MainActivityViewModel(
    application: Application,
    private val state: SavedStateHandle)
    : AndroidViewModel(application) {



    init {
        Log.v("APP_TAG", "MainActivityViewModel.init()")

    }



    companion object {
        val service = Retrofit.Builder()
            .baseUrl("https://lichess.org/api/puzzle/daily")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }

    val dailyPuzzle: LiveData<PuzzleInfo> = state.getLiveData(MAIN_ACTIVITY_VIEW_STATE)

    fun getDailyPuzzle() {
        service.getPuzzle().enqueue(object: Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                dailyPuzzle.value = response.body()
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                Log.e("APP_TAG", "onFailure", t)
            }
        })
    }
}