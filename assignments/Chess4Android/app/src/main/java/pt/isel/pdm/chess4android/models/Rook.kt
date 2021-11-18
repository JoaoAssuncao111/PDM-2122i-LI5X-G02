package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import java.util.*
import kotlin.collections.HashMap

class Rook(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum) {
    var isFirstMove: Boolean = true
    override fun myMoves(): EnumMap<Directions,List<Tile>> {
        var moves: EnumMap<Directions,List<Tile>> = EnumMap(Directions::class.java)
        moves[Directions.UP] = directionalMove(7,Directions.UP)
        moves[Directions.RIGHT] = directionalMove(7,Directions.RIGHT)
        moves[Directions.DOWN] = directionalMove(7,Directions.DOWN)
        moves[Directions.LEFT] = directionalMove(7,Directions.LEFT)
        return moves
    }


}
