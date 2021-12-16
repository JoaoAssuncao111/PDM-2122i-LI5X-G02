package pt.isel.pdm.chess4android

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityViewModel(
    application: Application,
) : AndroidViewModel(application) {
    companion object {
        val service: DailyPuzzleService = Retrofit.Builder()
            .baseUrl("https://lichess.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }


    val dailyPuzzleDTO: MutableLiveData<PuzzleInfoDTO> = MutableLiveData()


    fun getDailyPuzzle() {
        service.getPuzzle().enqueue(object : Callback<PuzzleInfoDTO> {
            override fun onResponse(call: Call<PuzzleInfoDTO>, response: Response<PuzzleInfoDTO>) {
                if (response.body() != null && response.isSuccessful) {


                }
            }

            override fun onFailure(call: Call<PuzzleInfoDTO>, t: Throwable) {
                Log.e("APP_TAG", "Request failed", t)
            }
        })
    }
}