package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import java.util.*


abstract class ChessPiece (val army: Army, var row: Int, var column: Int) {
    val maxRange: Int = 8
    abstract fun myMoves(): EnumMap<Directions,List<Tile>>

    fun directionalMove(range: Int, direction: Directions): List<Tile> {
        var list: MutableList<Tile> = mutableListOf()
        var possibleRow: Int = row
        var possibleColumn: Int = column
        repeat(range) {
            possibleRow += direction.row
            possibleColumn += direction.column
            if(possibleRow in 0 until maxRange && possibleColumn in 0 until maxRange) list.add(Tile(possibleRow,possibleColumn))
      }
        return list
    }

}