package pt.isel.pdm.chess4android


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

data class PuzzleInfo(val game: Game, val puzzle: Puzzle)


data class Game(val pgn: String)


data class Puzzle(val solution: String)

interface DailyPuzzleService {
    @GET("puzzle/daily/")
    fun getPuzzle(): Call<PuzzleInfo>
}
