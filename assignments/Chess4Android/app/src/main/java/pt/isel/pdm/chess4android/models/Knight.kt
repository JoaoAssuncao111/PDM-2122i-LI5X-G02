package pt.isel.pdm.chess4android.models

import java.util.*

class Knight(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum) {
    override fun myMoves(): EnumMap<Directions, List<Tile>> {
        var moves: EnumMap<Directions,List<Tile>> = EnumMap(Directions::class.java)
        moves[Directions.KNIGHT_UP_RIGHT] = directionalMove(1,Directions.KNIGHT_UP_RIGHT)
        moves[Directions.KNIGHT_UP_LEFT] = directionalMove(1,Directions.KNIGHT_UP_LEFT)
        moves[Directions.KNIGHT_LEFT_UP] = directionalMove(1,Directions.KNIGHT_LEFT_UP)
        moves[Directions.KNIGHT_LEFT_DOWN] = directionalMove(1,Directions.KNIGHT_LEFT_DOWN)
        moves[Directions.KNIGHT_RIGHT_UP] = directionalMove(1,Directions.KNIGHT_RIGHT_UP)
        moves[Directions.KNIGHT_RIGHT_DOWN] = directionalMove(1,Directions.KNIGHT_RIGHT_DOWN)
        moves[Directions.KNIGHT_DOWN_RIGHT] = directionalMove(1,Directions.KNIGHT_DOWN_RIGHT)
        moves[Directions.KNIGHT_DOWN_LEFT] = directionalMove(1,Directions.KNIGHT_DOWN_LEFT)
        return moves
    }
}