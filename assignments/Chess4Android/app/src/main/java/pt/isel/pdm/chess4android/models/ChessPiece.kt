package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army



abstract class ChessPiece (val army: Army, var row: Int, var column: Int) {
    private val maxRange: Int = 8
    abstract fun myMoves(): MutableList<List<Tile>>

    fun moveInDirection(range: Int,direction: Directions): List<Tile> {
        var list: MutableList<Tile> = mutableListOf()
        for (i in 1..range) {
            if(row+direction.row in 1 until maxRange && column+direction.column in 1 until maxRange) list.add(Tile(row,column))
      }
        return list
    }



}