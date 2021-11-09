package pt.isel.pdm.chess4android.models

import pt.isel.pdm.chess4android.Army
import pt.isel.pdm.chess4android.Piece

abstract class Pieces (val army: Army, val piece: Piece, var row: Int, var column: Int) {
    abstract fun move(row: Int, column: Int)

}