package pt.isel.pdm.chess4android.models

class Board() {
    private val side: Int = 8

    var puzzleSolution: MutableList<Pair<Tile,Tile>>? = null
    var isPuzzleCompleted = false
    var whiteKing: ChessPiece? = null
    var blackKing: ChessPiece? = null
    var currentArmy: Army = Army.WHITE
    var lastMoves: MutableList<Pair<Tile, ChessPiece>?> = mutableListOf()
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
                val currentTilePiece = getPieceAtTile(tile)
                if (piece is Pawn) {
                    //lets pawn move diagonally if there is an enemy piece there
                    if (key != Directions.UP && key != Directions.DOWN) {
                        if (currentTilePiece != null && currentTilePiece.army != piece.army) currentPieceLegalMoves.add(
                            tile
                        )
                    } else if (currentTilePiece == null) currentPieceLegalMoves.add(tile)
                    else break;
                    continue
                }
                //castling case
                if (piece is King) {
                    if (key == Directions.CASTLING_RIGHT || key == Directions.CASTLING_LEFT) {
                        if (currentTilePiece == null) {
                            continue
                        } else if (currentTilePiece is Rook && currentTilePiece.isFirstMove) currentPieceLegalMoves.add(
                            Tile(piece.row, piece.column + key.column)
                        )
                        else break

                    }
                    //Stopping king from moving into a check position
                    if (currentTilePiece?.army != piece.army) {
                        //if (getAllChecks(piece.army, tile).size != 0) continue
                    }
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

    fun makeMove(piece: ChessPiece, goalTile: Tile): Boolean {
        if(puzzleSolution != null){
            val pair = puzzleSolution!![0]
            if(Tile(piece.row,piece.column) != pair.first || goalTile != pair.second) return false
            puzzleSolution!!.removeFirst()
            if(puzzleSolution!!.isEmpty()) isPuzzleCompleted = true
        }

        if (piece is King && piece.isFirstMove) {
            if (goalTile == Tile(piece.row, piece.column + Directions.CASTLING_RIGHT.column)) {
                val rook: ChessPiece = (getPieceAtTile(Tile(piece.row, 7)))!!
                makeMove(rook, Tile(goalTile.row, goalTile.column - 1))
            } else if (goalTile == Tile(
                    piece.row,
                    piece.column + Directions.CASTLING_LEFT.column
                )
            ) {
                val rook: ChessPiece = (getPieceAtTile(Tile(piece.row, 0)))!!
                makeMove(rook, Tile(goalTile.row, goalTile.column + 1))
            }
        }
        lastMoves.add(Pair(Tile(piece.row, piece.column), piece))
        setPieceAtTile(goalTile, piece)
        setPieceAtTile(Tile(piece.row, piece.column), null)
        piece.column = goalTile.column
        piece.row = goalTile.row
        when (piece) {
            is Pawn -> piece.isFirstMove = false
            is King -> piece.isFirstMove = false
            is Rook -> piece.isFirstMove = false
        }
        return true

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

    fun decodePgn(pgn: String) {
        var movesToMake: List<String> = pgn.split(" ")
        for (move in movesToMake) {

            var formatedMove = move

            formatedMove = formatedMove.replace("+", "")
            formatedMove = formatedMove.replace("x", "")
            //Castling
            if (formatedMove.contains("O-")) {
                val king = if (currentArmy == Army.WHITE) whiteKing else blackKing
                val tile =
                    if (formatedMove == "O-O") Tile(
                        king!!.row,
                        king.column + Directions.CASTLING_RIGHT.column
                    ) else Tile(king!!.row, king.column + Directions.CASTLING_LEFT.column)
                makeMove(king, tile)
                switchTurns()
                continue
            }

            if (formatedMove.contains("=")) {}//PROMOTION
            val goalTile = decodeTile(formatedMove.substring(formatedMove.length-2,formatedMove.length))

             formatedMove = formatedMove.removeRange(formatedMove.length - 2, formatedMove.length)
            // if move had only goalTile
            if (formatedMove.isEmpty()) {
                moveByType(formatedMove, null, null, goalTile)
            }

            //if move had type of piece to be moved
            else if (formatedMove.length == 1) {
                if (formatedMove[0].isUpperCase()) {
                    moveByType(formatedMove, null, null, goalTile)
                } else moveByType("", formatedMove[0], null, goalTile)
                //if move had type and column of piece to be moved
            } else if (formatedMove.length == 2) {
                moveByType(formatedMove[0].toString(), formatedMove[1], null, goalTile)
                //if move had type, column and row of piece to be moved
            } else if (formatedMove.length == 3) {
                moveByType(formatedMove[0].toString(), formatedMove[1], formatedMove[2], goalTile)
            }
            switchTurns()
        }


    }

    private fun moveByType(string: String, column: Char?, row: Char?, tile: Tile) {
        val initialRow: Int = if (row != null) Character.getNumericValue(row) - 1 else 0
        val initialColumn: Int = if (column != null) Character.getNumericValue(column)-10 else 0
        val lastRow: Int = if (row != null) initialRow else 7
        val lastColumn: Int = if (column != null) initialColumn else 7
        for (i in initialRow..lastRow) {
            for (j in initialColumn..lastColumn) {
                val piece: ChessPiece? = getPieceAtTile(Tile(i, j))
                    if(piece == null || piece.army != currentArmy) continue
                when (string) {
                    "" -> if (piece !is Pawn) {continue}
                    "N" -> if (piece !is Knight){continue}
                    "B" -> if (piece !is Bishop){continue}
                    "Q" -> if (piece !is Queen){continue}
                    "R" -> if (piece !is Rook){continue}
                    "K" -> if (piece !is King){continue}
                }

                if (allLegalMoves(piece).contains(tile)){
                    makeMove(piece, tile)
                    return}
            }
        }

    }


    private fun decodeTile(move: String): Tile{
        return Tile(move[1].digitToInt()-1,Character.getNumericValue(move[0])-10)
    }

    private fun decodeSolutionMove(solution: Array<String>){
        puzzleSolution = mutableListOf<Pair<Tile,Tile>>()
        for(string in solution) {
            val initialTile = decodeTile(string.substring(0, 2))
            val goalTile = decodeTile(string.substring(2, 4))
            puzzleSolution!!.add(Pair(initialTile, goalTile))
        }
    }


    fun setupPuzzle(pgn: String,solution: Array<String>){
        decodePgn(pgn)
        decodeSolutionMove(solution)

    }



    //Puts piece at designated position on board
    private fun setPieceAtTile(tile: Tile, piece: ChessPiece?) {
        board[tile.row][tile.column] = piece
    }

    //returns the piece at the given tile
    fun getPieceAtTile(tile: Tile): ChessPiece? {
        return board[tile.row][tile.column]
    }

    fun isSameArmy(tile: Tile): Boolean {
        if (getPieceAtTile(tile)?.army != currentArmy) return false
        return true
    }

    fun isAllowedToMove(tile: Tile, legalMoves: List<Tile>): Boolean {
        return legalMoves.contains(tile)
    }

    fun switchTurns() {
        currentArmy = if (currentArmy == Army.WHITE) Army.BLACK else Army.WHITE
    }


}
