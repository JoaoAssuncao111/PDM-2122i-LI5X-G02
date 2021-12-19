package pt.isel.pdm.chess4android.history


import androidx.room.*
import pt.isel.pdm.chess4android.Game
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.Puzzle
import java.lang.reflect.Type


@Entity(tableName = "puzzle_history")
data class PuzzleInfoEntity(
    @PrimaryKey val id: String,
    val game: Game,
    val puzzle: Puzzle,
    val timestamp: Date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS)),
    val state: Boolean
) {
    fun isTodayPuzzle(): Boolean =
        timestamp.toInstant().compareTo(Instant.now().truncatedTo(ChronoUnit.DAYS)) == 0
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

    @TypeConverter
    fun fromSolution(value: String): Puzzle {
        val listType: Type = object : TypeToken<Array<String?>?>() {}.type
        return Puzzle(Gson().fromJson(value, listType))
    }

    @TypeConverter
    fun puzzleToSolution(puzzle: Puzzle): String {
        val gson = Gson()
        return gson.toJson(puzzle.solution)
    }

    @TypeConverter
    fun fromPGN(pgn:String) = Game(pgn)

    @TypeConverter
    fun gameToPGN(game:Game) = game.pgn
}
@Dao
interface PuzzleHistoryDao {
    @Insert
    fun insert(quote: PuzzleInfoEntity)

    @Delete
    fun delete(quote: PuzzleInfoEntity)

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT 100")
    fun getAll(): List<PuzzleInfoEntity>

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT :count")
    fun getLast(count: Int): List<PuzzleInfoEntity>
}

@Database(entities = [PuzzleInfoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun getPuzzleHistoryDao(): PuzzleHistoryDao
}

