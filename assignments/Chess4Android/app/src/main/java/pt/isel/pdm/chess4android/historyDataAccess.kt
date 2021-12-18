package pt.isel.pdm.chess4android


import androidx.room.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Entity(tableName = "puzzle_history")
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val game: Game,
    val puzzle: Puzzle,
    val timestamp: Date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS))
) {
    fun isTodayPuzzle(): Boolean =
        timestamp.toInstant().compareTo(Instant.now().truncatedTo(ChronoUnit.DAYS)) == 0
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}
@Dao
interface PuzzleHistoryDao {
    @Insert
    fun insert(quote: PuzzleEntity)

    @Delete
    fun delete(quote: PuzzleEntity)

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT 100")
    fun getAll(): List<PuzzleEntity>

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT :count")
    fun getLast(count: Int): List<PuzzleEntity>
}

@Database(entities = [PuzzleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun getPuzzleHistoryDao(): PuzzleHistoryDao
}

