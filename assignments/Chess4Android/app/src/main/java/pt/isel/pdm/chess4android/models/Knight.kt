package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army

class Knight(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum) {
    override fun myMoves(): MutableList<List<Tile>> {
        var moves: MutableList<List<Tile>> = mutableListOf()
        moves.add(directionalMove(1,Directions.KNIGHT_UP_RIGHT))
        moves.add(directionalMove(1,Directions.KNIGHT_UP_LEFT))
        moves.add(directionalMove(1,Directions.KNIGHT_LEFT_UP))
        moves.add(directionalMove(1,Directions.KNIGHT_LEFT_DOWN))
        moves.add(directionalMove(1,Directions.KNIGHT_RIGHT_UP))
        moves.add(directionalMove(1,Directions.KNIGHT_RIGHT_DOWN))
        moves.add(directionalMove(1,Directions.KNIGHT_DOWN_RIGHT))
        moves.add(directionalMove(1,Directions.KNIGHT_DOWN_LEFT))
        return moves
    }
}