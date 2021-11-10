package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army

class Queen(army: Army, row: Int, collum: Int) : ChessPiece(army, row, collum){
    override fun myMoves(): MutableList<List<Tile>> {
        TODO("Not yet implemented")
    }
}