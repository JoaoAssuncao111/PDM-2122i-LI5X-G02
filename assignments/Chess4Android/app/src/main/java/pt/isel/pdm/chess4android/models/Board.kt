package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import java.sql.RowId

class Board() {
    val side: Int = 8

    private var board = Array<Array<ChessPiece?>>(side) { i -> Array(8) { j -> null } }

    private fun addToBoard(p: ChessPiece){
        board[p.row][p.column]
    }
    fun startGame() {
        for (i in 0 until 2){
            addToBoard(Rook(Army.WHITE,1,1 + i*7))
            addToBoard(Rook(Army.BLACK,8,1 + i*7))
            addToBoard(Knight(Army.WHITE,1,2 + i*5))
            addToBoard(Knight(Army.BLACK,8,2 + i*5))
            addToBoard(Bishop(Army.WHITE,1,3 + i*3))
            addToBoard(Bishop(Army.BLACK,8,3 + i*3))
        }
        for(i in 1 until 9){
            addToBoard(Pawn(Army.WHITE,2,i))
            addToBoard(Pawn(Army.BLACK,7,i))
        }
    }


    fun canMove(piece: ChessPiece,newTile: Tile): Boolean{
        if(piece.row == newTile.row && piece.column == newTile.column) return false
        val allMoves = piece.myMoves()
        repeat(allMoves.size){
            if(!allMoves.iterator().next().contains(newTile)) return false
        }



    }


    fun pieceAtTile(tile: Tile): ChessPiece? {
        return board[tile.row][tile.column]
    }


}