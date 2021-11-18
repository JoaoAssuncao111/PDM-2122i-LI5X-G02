package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army


class Board() {
    private val side: Int = 8


    var whiteKing: ChessPiece? = null
    var blackKing: ChessPiece? = null
    var currentArmy: Army = Army.WHITE
    var lastMove: Pair<Tile, ChessPiece>? = null
    var board = Array<Array<ChessPiece?>>(side) { i -> Array(8) { j -> null } }

    private fun addToBoard(p: ChessPiece) {
        board[p.row][p.column] = p
    }

    fun startGame() {
        for (i in 0 until 2) {
            addToBoard(Rook(Army.WHITE, 0, 0 + i * 7))
            addToBoard(Rook(Army.BLACK, 7, 0 + i * 7))
            addToBoard(Knight(Army.WHITE, 0, 1 + i * 5))
            addToBoard(Knight(Army.BLACK, 7, 1 + i * 5))
            addToBoard(Bishop(Army.WHITE, 0, 2 + i * 3))
            addToBoard(Bishop(Army.BLACK, 7, 2 + i * 3))
        }
        for (i in 0 until 8) {
            addToBoard(Pawn(Army.WHITE, 1, i))
            addToBoard(Pawn(Army.BLACK, 6, i))
        }

        whiteKing = King(Army.WHITE, 0, 4)
        blackKing = King(Army.BLACK, 7, 4)

        whiteKing?.let { addToBoard(it) }
        blackKing?.let { addToBoard(it) }

        addToBoard(Queen(Army.WHITE, 0, 3))
        addToBoard(Queen(Army.BLACK, 7, 3))

    }


    fun allLegalMoves(piece: ChessPiece): MutableList<Tile> {
        val currentPieceLegalMoves = mutableListOf<Tile>()
        val allMoves = piece.myMoves()

        for ((key, value) in allMoves) {
            for (tile in value) {
                var currentTilePiece = getPieceAtTile(tile)
                if (piece is Pawn) {
                    //lets pawn move diagonally if there is an enemy piece there
                    if (key != Directions.UP && key != Directions.DOWN) {
                        if (currentTilePiece != null && currentTilePiece.army != piece.army) currentPieceLegalMoves.add(tile)
                    } else if (getPieceAtTile(tile) == null) currentPieceLegalMoves.add(tile)
                    continue
                }
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

    fun makeMove(piece: ChessPiece, goalTile: Tile){
        lastMove = Pair(Tile(piece.row,piece.column),piece)
        setPieceAtTile(goalTile,piece)
        setPieceAtTile(Tile(piece.row,piece.column), null)
        piece.column = goalTile.column
        piece.row = goalTile.row
    }

    //return whether or not a piece can stop the king from its army from being attacked
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

    //Puts piece at designated position on board
    fun setPieceAtTile(tile: Tile, piece: ChessPiece?){
        board[tile.row][tile.column] = piece
    }

    //returns the piece at the given tile
    fun getPieceAtTile(tile: Tile): ChessPiece? {
        return board[tile.row][tile.column]
    }

    fun isSameArmy(tile: Tile): Boolean{
        if(getPieceAtTile(tile)?.army != currentArmy) return false
        return true
    }

    fun isAllowedToMove(tile: Tile, legalMoves: List<Tile>): Boolean{
        return legalMoves.contains(tile)
    }

    fun turnSwitch(){
        currentArmy = if(currentArmy == Army.WHITE) Army.BLACK else Army.WHITE
    }


}
