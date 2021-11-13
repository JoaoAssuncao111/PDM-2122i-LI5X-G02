package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army


class Board() {
    private val side: Int = 8
    private val currentPieceLegalMoves = mutableListOf<Tile>()

    var whiteKing: ChessPiece? = null
    var blackKing: ChessPiece? = null

    private var board = Array<Array<ChessPiece?>>(side) { i -> Array(8) { j -> null } }

    private fun addToBoard(p: ChessPiece) {
        board[p.row][p.column]
    }

    fun startGame() {
        for (i in 0 until 2) {
            addToBoard(Rook(Army.WHITE, 1, 1 + i * 7))
            addToBoard(Rook(Army.BLACK, 8, 1 + i * 7))
            addToBoard(Knight(Army.WHITE, 1, 2 + i * 5))
            addToBoard(Knight(Army.BLACK, 8, 2 + i * 5))
            addToBoard(Bishop(Army.WHITE, 1, 3 + i * 3))
            addToBoard(Bishop(Army.BLACK, 8, 3 + i * 3))
        }
        for (i in 1 until 9) {
            addToBoard(Pawn(Army.WHITE, 2, i))
            addToBoard(Pawn(Army.BLACK, 7, i))
        }

        whiteKing = King(Army.WHITE, 1, 4)
        blackKing = King(Army.BLACK, 8, 4)

        whiteKing?.let { addToBoard(it) }
        blackKing?.let { addToBoard(it) }

        addToBoard(Queen(Army.WHITE, 1, 5))
        addToBoard(Queen(Army.BLACK, 1, 5))

    }


    fun allLegalMoves(piece: ChessPiece): MutableList<Tile> {

        val allMoves = piece.myMoves()
        for (list in allMoves) {
            for (tile in list) {
                var currentTilePiece = pieceAtTile(tile)
                //Stopping king from moving into a check position
                if (piece is King && currentTilePiece?.army != piece.army) {
                    if (getAllChecks(piece.army, tile).size != 0) continue
                }

                if (currentTilePiece == null) {
                    currentPieceLegalMoves.add(tile)
                } else if (currentTilePiece.army != piece.army) {
                    currentPieceLegalMoves.add(tile)
                    break
                } else break
            }
        }

        return currentPieceLegalMoves
    }

    //return wether or not a piece can stop the king from its army from being attacked
    fun canMoveToBlock(piece: ChessPiece, goalTiles: List<Tile>): Boolean {
        for (tile in goalTiles)
            if (allLegalMoves(piece).contains(tile)) return true
        return false
    }

    //Returns all pieces that attack a certain tile (army refers to the one being attacked)
    fun getAllChecks(army: Army, tile: Tile): MutableList<ChessPiece> {
        var piecesThatCheck: MutableList<ChessPiece> = mutableListOf()
        for (row in board) {
            for (piece in row) {
                if (piece != null && piece.army != army) {
                    if (allLegalMoves(piece).contains(tile)) piecesThatCheck.add(piece)
                }
            }
        }
        return piecesThatCheck
    }

    //returns the piece at the given tile
    fun pieceAtTile(tile: Tile): ChessPiece? {
        return board[tile.row][tile.column]
    }


}