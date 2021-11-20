package pt.isel.pdm.chess4android.models

enum class Directions (val row: Int, val column: Int){
    UP(1,0),
    DOWN(-1,0),
    LEFT(0,-1),
    RIGHT(0,+1),
    UP_LEFT(1,-1),
    UP_RIGHT(1,1),
    DOWN_LEFT(-1,-1),
    DOWN_RIGHT(-1,+1),
    KNIGHT_UP_RIGHT(2,1),
    KNIGHT_UP_LEFT(2,-1),
    KNIGHT_LEFT_UP(1,-2),
    KNIGHT_LEFT_DOWN(-1,-2),
    KNIGHT_RIGHT_UP(1,2),
    KNIGHT_RIGHT_DOWN(-1,2),
    KNIGHT_DOWN_RIGHT(-2,1),
    KNIGHT_DOWN_LEFT(-2,-1),
    CASTLING_LEFT(0,-2),
    CASTLING_RIGHT(0,2),

}

