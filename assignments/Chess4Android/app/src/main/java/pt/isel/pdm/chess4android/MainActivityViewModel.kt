package pt.isel.pdm.chess4android

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import pt.isel.pdm.chess4android.models.Board
import pt.isel.pdm.chess4android.models.ChessPiece
import pt.isel.pdm.chess4android.models.Tile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val MAIN_ACTIVITY_VIEW_STATE = "MainActivity.ViewState"

class MainActivityViewModel(
    application: Application,
    private val state: SavedStateHandle
) : AndroidViewModel(application) {

    var currentPiece: ChessPiece? = null
    var currentPieceMoves: List<Tile>? = null
    var board: Board? = null

    init {
        board = Board()
        board!!.startGame()
        Log.v("APP_TAG", "MainActivityViewModel.init()")

    }


    fun checkBoardOnClick(row: Int, column: Int) {
        if (board == null) return
        val tile = Tile(row, column)
        //Verifies if the touch was supposed to select a piece
        if (board!!.isSameArmy(tile)) {
            val tempPiece = board!!.getPieceAtTile(tile)
            if (currentPiece != tempPiece) {
                currentPiece = tempPiece
                currentPieceMoves = board!!.allLegalMoves(currentPiece!!)
            }
            //Verifies if the touch was supposed to move a piece
        } else if (currentPiece != null) {
            if (currentPieceMoves != null && currentPieceMoves!!.isNotEmpty()) {
                if (board!!.isAllowedToMove(tile, currentPieceMoves!!)) {
                    board!!.makeMove(currentPiece!!, tile)
                    currentPiece = null
                    currentPieceMoves = null
                    //TODO("Verify game-ending situations")
                    board!!.turnSwitch()
                    //post value board live data

                }
            }
        }


    }

    fun selectPiece(row: Int, column: Int): Boolean {
        if (board == null) return false
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
            if (board == null) return false
            val tile = Tile(row, column)
            //Verifies if the touch was supposed to move a piece
            if (currentPiece != null) {
                if (currentPieceMoves != null && currentPieceMoves!!.isNotEmpty()) {
                    if (board!!.isAllowedToMove(tile, currentPieceMoves!!)) {
                        board!!.makeMove(currentPiece!!, tile)
                        currentPiece = null
                        currentPieceMoves = null
                        return true
                    }
                }
            }
            return false
        }
        fun isEndOfGame(){
            //TODO("Verify game-ending situations")
            board!!.turnSwitch()
        }



    }