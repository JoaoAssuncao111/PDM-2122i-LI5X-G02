package pt.isel.pdm.chess4android

import retrofit2.Call
import retrofit2.http.GET

data class DailyPuzzle()

interface DailyPuzzleService{
    @GET("/")
    fun getDailyPuzzle(): Call<DailyPuzzle>
}
