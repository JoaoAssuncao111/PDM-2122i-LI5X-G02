package pt.isel.pdm.chess4android.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.PuzzleInfo
import pt.isel.pdm.chess4android.models.Board
import pt.isel.pdm.chess4android.models.ChessPiece
import pt.isel.pdm.chess4android.models.Tile


class GameActivityViewModel(
    application: Application,
) : AndroidViewModel(application) {

    var currentPiece: ChessPiece? = null
    var currentPieceMoves: List<Tile>? = null
    var board: Board? = null

    init {
        board = Board()
        board!!.startGame()

        Log.v("APP_TAG", "MainActivityViewModel.init()")

    }


    fun setupPuzzle(puzzleInfo : PuzzleInfo){
        board!!.setupPuzzle(puzzleInfo.game.pgn,puzzleInfo.puzzle.solution)
    }



    fun selectPiece(row: Int, column: Int): Boolean {
        if (board == null || board!!.completed) return false
        val tile = Tile(row, column)
        //Verifies if the touch was supposed to select a piece
        if (board!!.isSameArmy(tile)) {
            val tempPiece = board!!.getPieceAtTile(tile)
            if (currentPiece != tempPiece) {
                currentPiece = tempPiece
                currentPieceMoves = board!!.allLegalMoves(currentPiece!!)
            }
            return true
        }
        return false
    }

    //returns true if move was successful
    fun movePiece(row: Int, column: Int): Boolean {
        if (board == null || board!!.completed) return false
        val tile = Tile(row, column)
        //Verifies if the touch was supposed to move a piece
        if (currentPiece != null) {
            if (currentPieceMoves != null && currentPieceMoves!!.isNotEmpty()) {
                if (board!!.isAllowedToMove(tile, currentPieceMoves!!)) {
                    if (board!!.makeMove(currentPiece!!, tile)) {
                        currentPiece = null
                        currentPieceMoves = null
                        return true
                    }
                }
            }
        }
        return false
    }

    fun makeMoveIfPuzzle(): Boolean {
        if (board!!.puzzleSolution != null && board!!.puzzleSolution!!.isNotEmpty()) {
            board!!.switchTurns()
            val pair = board!!.puzzleSolution!!.first()
            val piece = board!!.getPieceAtTile(pair.first)
            board!!.makeMove(piece!!, pair.second)
            board!!.switchTurns()
            return true
        }
        return false
    }

    fun getAllChecks(): MutableList<ChessPiece>{
        return board!!.isChecked()
    }

    fun endTurn(){
        board!!.switchTurns()
    }

    fun isCompleted(): Boolean {
        return board!!.completed
    }


    fun isEndOfGame(pieces: MutableList<ChessPiece>) : Boolean{
        //"Verify game-ending situations"
        if(board!!.isCheckmate(pieces)){
            board!!.completed = true
            return true
        }
        return false
    }


}
