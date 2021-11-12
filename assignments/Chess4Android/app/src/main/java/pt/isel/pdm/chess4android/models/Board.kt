package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army


class Board() {
    val side: Int = 8
    val currentPieceLegalMoves = mutableListOf<Tile>()

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

        whiteKing = King(Army.WHITE,1,4)
        blackKing = King(Army.BLACK,8,4)
        whiteKing?.let { addToBoard(it) }
        blackKing?.let { addToBoard(it) }
        addToBoard(Queen(Army.WHITE,1,5))
        addToBoard(Queen(Army.BLACK,1,5))
        
    }


    fun allLegalMoves(piece: ChessPiece): MutableList<Tile> {
        val allMoves = piece.myMoves()
        for (list in allMoves) {
            for (tile in list) {
                var currentTilePiece = pieceAtTile(tile)
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


    fun pieceAtTile(tile: Tile): ChessPiece? {
        return board[tile.row][tile.column]
    }


}