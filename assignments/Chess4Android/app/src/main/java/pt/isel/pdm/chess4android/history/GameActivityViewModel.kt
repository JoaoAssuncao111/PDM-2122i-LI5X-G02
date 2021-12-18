package pt.isel.pdm.chess4android.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
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

    /*
    fun getDailyPuzzle() {
        service.getPuzzle().enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                if (response.body() != null && response.isSuccessful) {
                    board!!.setupPuzzle(
                        response.body()!!.game.pgn,
                        response.body()!!.puzzle.solution
                    )
                    dailyPuzzle.postValue(response.body())
                    //state.set(MAIN_ACTIVITY_VIEW_STATE, response.body())
                }
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
                Log.e("APP_TAG", "Request failed", t)
            }
        })
    }
    */



    fun selectPiece(row: Int, column: Int): Boolean {
        if (board == null || board!!.isPuzzleCompleted) return false
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
        if (board == null || board!!.isPuzzleCompleted) return false
        val tile = Tile(row, column)
        //Verifies if the touch was supposed to move a piece
        if (currentPiece != null) {
            if (currentPieceMoves != null && currentPieceMoves!!.isNotEmpty()) {
                if (board!!.isAllowedToMove(tile, currentPieceMoves!!)) {
                    if (board!!.makeMove(currentPiece!!, tile)) {
                        currentPiece = null
                        currentPieceMoves = null
                        board!!.switchTurns()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun makeMoveIfPuzzle(): Boolean {
        if (board!!.puzzleSolution != null && board!!.puzzleSolution!!.isNotEmpty()) {
            val pair = board!!.puzzleSolution!!.first()
            val piece = board!!.getPieceAtTile(pair.first)
            board!!.makeMove(piece!!, pair.second)
            board!!.switchTurns()
            return true
        }
        return false
    }


        fun isEndOfGame() {
            //TODO("Verify game-ending situations")
            board!!.switchTurns()
        }


    }
