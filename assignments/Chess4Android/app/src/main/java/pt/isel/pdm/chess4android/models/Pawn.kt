package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import java.util.*

class Pawn(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum){
    var isFirstMove: Boolean = true

    override fun myMoves(): EnumMap<Directions, List<Tile>> {
        var moves: EnumMap<Directions,List<Tile>> = EnumMap(Directions::class.java)

        if(army == Army.WHITE) {
            if(isFirstMove) moves[Directions.UP] = directionalMove(2,Directions.UP) else moves[Directions.UP] = directionalMove(1,Directions.UP)
            moves[Directions.UP_LEFT] = directionalMove(1, Directions.UP_LEFT)
            moves[Directions.UP_RIGHT] = directionalMove(1, Directions.UP_RIGHT)
        } else {
            if(isFirstMove) moves[Directions.DOWN] = directionalMove(2,Directions.DOWN) else moves[Directions.DOWN] = directionalMove(1,Directions.DOWN)
            moves[Directions.DOWN_LEFT] = directionalMove(1, Directions.DOWN_LEFT)
            moves[Directions.DOWN_RIGHT] = directionalMove(1, Directions.DOWN_RIGHT)

        }
        return moves

    }
}