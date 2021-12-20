package pt.isel.pdm.chess4android


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

@Parcelize
data class DailyPuzzleInfoDTO(val puzzleInfo: PuzzleInfo, val date: String, val state: Boolean): Parcelable

@Parcelize
data class PuzzleInfo(val game: Game, val puzzle: Puzzle) :Parcelable

@Parcelize
data class Game(val pgn: String): Parcelable

@Parcelize
data class Puzzle(val solution: Array<String>): Parcelable

interface DailyPuzzleInfoService {
    @GET("puzzle/daily")
    fun getPuzzleInfo(): Call<PuzzleInfo>
}

/**
 * Represents errors while accessing the remote API. Instead of tossing around Retrofit errors,
 * we can use this exception to wrap them up.
 */
class ServiceUnavailable(message: String = "", cause: Throwable? = null) : Exception(message, cause)