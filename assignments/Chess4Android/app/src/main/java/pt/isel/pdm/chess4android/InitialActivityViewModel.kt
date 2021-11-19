package pt.isel.pdm.chess4android

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InitialActivityViewModel(
    application: Application,
    ) : AndroidViewModel(application) {

     companion object {
        val service = Retrofit.Builder()
            .baseUrl("https://lichess.org/api/puzzle/daily")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)

     }


    val dailyPuzzle: MutableLiveData<PuzzleInfo> = MutableLiveData()


    fun getDailyPuzzle() {
        service.getPuzzle().enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                dailyPuzzle.postValue(response.body())
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                Log.e("APP_TAG", "Request failed", t)
            }
        })
    }


}