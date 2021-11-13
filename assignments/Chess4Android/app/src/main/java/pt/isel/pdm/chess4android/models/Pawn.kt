package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army

class Pawn(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum){
    var isFirstMove: Boolean = true

    override fun myMoves(): MutableList<List<Tile>> {
        var moves: MutableList<List<Tile>> = mutableListOf()

        if(army == Army.WHITE) {
            if(isFirstMove) moves.add(directionalMove(2,Directions.UP)) else moves.add(directionalMove(1,Directions.UP))
            moves.add(directionalMove(1, Directions.UP_LEFT))
            moves.add(directionalMove(1, Directions.UP_RIGHT))
        } else {
            if(isFirstMove) moves.add(directionalMove(2,Directions.DOWN)) else moves.add(directionalMove(1,Directions.DOWN))
            moves.add(directionalMove(1, Directions.DOWN_LEFT))
            moves.add(directionalMove(1, Directions.DOWN_RIGHT))

        }
        return moves

    }
}