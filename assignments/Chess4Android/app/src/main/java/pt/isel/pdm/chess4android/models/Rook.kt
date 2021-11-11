package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army

class Rook(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum) {
    override fun myMoves(): MutableList<List<Tile>> {
        var moves: MutableList<List<Tile>> = mutableListOf()
        moves.add(directionalMove(7,Directions.UP))
        moves.add(directionalMove(7,Directions.RIGHT))
        moves.add(directionalMove(7,Directions.DOWN))
        moves.add(directionalMove(7,Directions.LEFT))
        return moves
    }


}
