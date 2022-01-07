package pt.isel.pdm.chess4android.history


import androidx.room.*
import pt.isel.pdm.chess4android.Game
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pt.isel.pdm.chess4android.Puzzle
import java.lang.reflect.Type


@Entity(tableName = "puzzle_history")
data class PuzzleInfoEntity(
    @PrimaryKey val id: Long,
    val puzzleInfo: String,
    val state: Boolean,
) {

}

class Converters {
    
}

@Dao
interface PuzzleHistoryDao {
    @Insert
    fun insert(puzzle: PuzzleInfoEntity)

    @Delete
    fun delete(puzzle: PuzzleInfoEntity)

    @Update
    fun update(puzzle: PuzzleInfoEntity)

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT 100")
    fun getAll(): List<PuzzleInfoEntity>

    @Query("SELECT * FROM puzzle_history ORDER BY id DESC LIMIT :count")
    fun getLast(count: Int): List<PuzzleInfoEntity>
}

@Database(entities = [PuzzleInfoEntity::class], version = 1)
//TypeConverters(Converters::class)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun getPuzzleHistoryDao(): PuzzleHistoryDao
}

