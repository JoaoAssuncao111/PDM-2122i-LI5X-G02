package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import java.util.*

class King(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum) {
    override fun myMoves(): EnumMap<Directions, List<Tile>> {
        var moves: EnumMap<Directions,List<Tile>> = EnumMap(Directions::class.java)
        moves[Directions.UP] = directionalMove(1,Directions.UP)
        moves[Directions.UP_RIGHT] = directionalMove(1,Directions.UP_RIGHT)
        moves[Directions.RIGHT] = directionalMove(1,Directions.RIGHT)
        moves[Directions.DOWN_RIGHT] = directionalMove(1,Directions.DOWN_RIGHT)
        moves[Directions.DOWN] = directionalMove(1,Directions.DOWN)
        moves[Directions.DOWN_LEFT] = directionalMove(1,Directions.DOWN_LEFT)
        moves[Directions.LEFT] = directionalMove(1,Directions.LEFT)
        moves[Directions.UP_LEFT] = directionalMove(1,Directions.UP_LEFT)
        return moves
    }
}