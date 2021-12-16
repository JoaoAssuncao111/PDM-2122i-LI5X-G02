package pt.isel.pdm.chess4android


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

@Parcelize
data class DailyPuzzleInfoDTO(val puzzleInfoDTO: PuzzleInfoDTO, val date: String): Parcelable

@Parcelize
data class PuzzleInfoDTO(val game: Game, val puzzle: Puzzle) :Parcelable

@Parcelize
data class Game(val pgn: String): Parcelable

@Parcelize
data class Puzzle(val solution: Array<String>): Parcelable

interface DailyPuzzleService {
    @GET("puzzle/daily")
    fun getPuzzle(): Call<PuzzleInfoDTO>
}
