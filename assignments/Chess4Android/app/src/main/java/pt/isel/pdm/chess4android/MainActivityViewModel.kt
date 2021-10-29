package pt.isel.pdm.chess4android

import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityViewModel : ViewModel() {

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

    fun getDailyPuzzle(completion: (String) -> Unit) {
        service.getDailyPuzzle().enqueue(object: Callback<DailyPuzzle> {
            override fun onResponse(call: Call<DailyPuzzle>, response: Response<DailyPuzzle>) {
                completion(response.body()?.text ?: "")
            }

            override fun onFailure(call: Call<DailyPuzzle>, t: Throwable) {
                Log.e("APP_TAG", "onFailure", t)
            }
        })
    }
}