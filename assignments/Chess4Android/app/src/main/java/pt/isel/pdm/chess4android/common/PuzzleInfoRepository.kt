package pt.isel.pdm.chess4android.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.*
import pt.isel.pdm.chess4android.history.PuzzleHistoryDao
import pt.isel.pdm.chess4android.history.PuzzleInfoEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Extension function of [PuzzleInfoEntity] to conveniently convert it to a [DailyPuzzleInfoDTO] instance.
 * Only relevant for this activity.
 */

fun DailyPuzzleInfoDTO.toPuzzleInfoEntity() = PuzzleInfoEntity(
    id = this.date, puzzleInfo = Gson().toJson(this.puzzleInfo), state = this.state
)
fun PuzzleInfoEntity.toDailyPuzzleInfoDTO() = DailyPuzzleInfoDTO(
    puzzleInfo = Gson().fromJson(this.puzzleInfo, PuzzleInfo::class.java), date = this.id, state = this.state
)
/**
 * Repository for the Quote Of Day
 * It's role is the one described here: https://developer.android.com/jetpack/guide
 *
 * The repository operations include I/O (network and/or DB accesses) and are therefore asynchronous.
 * For now, and for teaching purposes, we use a callback style to define those operations. Later on
 * we will use Kotlin's way: suspending functions.
 */
class PuzzleInfoRepository(
    private val dailyPuzzleInfoService: DailyPuzzleInfoService,
    private val puzzleHistoryDao: PuzzleHistoryDao
) {

    /**
     * Asynchronously gets the daily quote from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncMaybeGetDailyPuzzleFromDB(callback: (Result<PuzzleInfoEntity?>) -> Unit) {
        callbackAfterAsync(callback) {
            puzzleHistoryDao.getLast(1).firstOrNull()
        }
    }

    /**
     * Asynchronously gets the daily quote from the remote API.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncGetDailyPuzzleFromAPI(callback: (Result<DailyPuzzleInfoDTO>) -> Unit) {
        dailyPuzzleInfoService.getPuzzleInfo().enqueue(
            object: Callback<PuzzleInfo> {
                override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                    //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: onResponse ")
                    val info = response.body()
                    val result =
                        if (info != null && response.isSuccessful)
                            Result.success(
                                DailyPuzzleInfoDTO(
                                info, Date.from(
                                Instant.now().truncatedTo(ChronoUnit.DAYS)).toString(), false))
                        else
                            Result.failure(ServiceUnavailable())
                    callback(result)
                }

                override fun onFailure(call: Call<PuzzleInfo>, error: Throwable) {
                    //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: onFailure ")
                    callback(Result.failure(ServiceUnavailable(cause = error)))
                }
            })
    }

    /**
     * Asynchronously saves the daily quote to the local DB.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncSaveToDB(dto: DailyPuzzleInfoDTO, callback: (Result<Unit>) -> Unit = { }) {
        callbackAfterAsync(callback) {
            val entity = dto.toPuzzleInfoEntity();
            puzzleHistoryDao.insert(
                entity
            )
        }
    }

    /**
     * Asynchronously gets the quote of day, either from the local DB, if available, or from
     * the remote API.
     *
     * @param mustSaveToDB  indicates if the operation is only considered successful if all its
     * steps, including saving to the local DB, succeed. If false, the operation is considered
     * successful regardless of the success of saving the quote in the local DB (the last step).
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD
     *
     * Using a boolean to distinguish between both options is a questionable design decision.
     */
    fun fetchDailyPuzzle(mustSaveToDB: Boolean = false, callback: (Result<DailyPuzzleInfoDTO>) -> Unit) {
        asyncMaybeGetDailyPuzzleFromDB { maybeEntity ->
            val maybePuzzle = maybeEntity.getOrNull()
            if (maybePuzzle != null) {
                //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Got daily quote from local DB")
                callback(Result.success(maybePuzzle.toDailyPuzzleInfoDTO()))
            }
            else {
                asyncGetDailyPuzzleFromAPI { apiResult ->
                    apiResult.onSuccess { puzzleDto ->
                        //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Got daily quote from API")
                        asyncSaveToDB(puzzleDto) { saveToDBResult ->
                            saveToDBResult.onSuccess {
                                //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: Saved daily quote to local DB")
                                callback(Result.success(puzzleDto))
                            }
                                .onFailure {
                                    //Log.e(APP_TAG, "Thread ${Thread.currentThread().name}: Failed to save daily quote to local DB", it)
                                    callback(if(mustSaveToDB) Result.failure(it) else Result.success(puzzleDto))
                                }
                        }
                    }
                }
            }
        }
    }
}