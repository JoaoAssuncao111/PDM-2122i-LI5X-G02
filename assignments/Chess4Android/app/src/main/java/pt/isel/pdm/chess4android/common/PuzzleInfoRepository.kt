package pt.isel.pdm.chess4android.common

import pt.isel.pdm.chess4android.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Extension function of [PuzzleEntity] to conveniently convert it to a [DailyPuzzleInfoDTO] instance.
 * Only relevant for this activity.
 */


fun PuzzleEntity.toDailyPuzzleInfoDTO() = DailyPuzzleInfoDTO(
    puzzleInfoDTO = PuzzleInfoDTO(game = this.game, this.puzzle),
    date = this.id, state = false
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
    private val dailyPuzzleService: DailyPuzzleService,
    private val puzzleHistoryDao: PuzzleHistoryDao
) {

    /**
     * Asynchronously gets the daily quote from the local DB, if available.
     * @param callback the function to be called to signal the completion of the
     * asynchronous operation, which is called in the MAIN THREAD.
     */
    private fun asyncMaybeGetDailyPuzzleFromDB(callback: (Result<PuzzleEntity?>) -> Unit) {
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
        dailyPuzzleService.getPuzzle().enqueue(
            object: Callback<DailyPuzzleInfoDTO> {
                override fun onResponse(call: Call<DailyPuzzleInfoDTO>, response: Response<DailyPuzzleInfoDTO>) {
                    //Log.v(APP_TAG, "Thread ${Thread.currentThread().name}: onResponse ")
                    val dailyPuzzle: DailyPuzzleInfoDTO? = response.body()
                    val result =
                        if (dailyPuzzle != null && response.isSuccessful)
                            Result.success(dailyPuzzle)
                        else
                            Result.failure(ServiceUnavailable())
                    callback(result)
                }

                override fun onFailure(call: Call<DailyPuzzleInfoDTO>, error: Throwable) {
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
            puzzleHistoryDao.insert(
                PuzzleEntity(
                    id = dto.date,
                    game = dto.puzzleInfoDTO.game,
                    puzzle = dto.puzzleInfoDTO.puzzle,
                    state = dto.state
                )
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
                    callback(apiResult)
                }
            }
        }
    }
}